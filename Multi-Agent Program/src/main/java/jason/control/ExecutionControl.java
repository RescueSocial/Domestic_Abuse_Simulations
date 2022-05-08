package jason.control;

import jason.runtime.RuntimeServices;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Base class for the user implementation of execution control.
 *
 * <p>This default implementation synchronise the agents execution, i.e.,
 * each agent will perform its next reasoning cycle only when all agents have
 * finished its reasoning cycle.
 *
 * <p>Execution sequence:
 *    <ul><li>setExecutionControlInfraTier,
 *        <li>init,
 *        <li>(receivedFinishedCycle)*,
 *        <li>stop.
 *    </ul>
 */
public class ExecutionControl {

    protected ExecutionControlInfraTier infraControl = null;

    private Set<String> finished = new HashSet<String>(); // the agents that have finished its reasoning cycle
    private volatile int     cycleNumber = 0;
    private volatile boolean runningCycle = true;
    private volatile boolean isRunning    = true;

    private int nbAgs = -1;

    private Lock lock = new ReentrantLock();
    private Condition agFinishedCond = lock.newCondition();
    private RuntimeServices runtime;

    protected static Logger logger = Logger.getLogger(ExecutionControl.class.getName());

    public ExecutionControl() {

        // create a thread to wait ag Finished signals
        new Thread("ExecControlWaitAgFinish") {
            public void run() {
                lock.lock();
                try {
                    while (isRunning) {
                        try {
                            boolean to = !agFinishedCond.await(getCycleTimeout(), TimeUnit.MILLISECONDS); // waits signal
                            if (!isRunning)
                                break;
                            if (runtime != null && runningCycle) {
                                runningCycle = false;
                                allAgsFinished();
                            }

                            // update number of agents if finished by timeout
                            if (to) {
                                if (logger.isLoggable(Level.FINE)) logger.fine("Cycle "+getCycleNumber()+" finished by timeout!");
                                updateNumberOfAgents();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }
        } .start();
    }

    /** returns the maximum number of milliseconds of a cycle */
    protected int getCycleTimeout() {
        return 5000;
    }

    protected void startNewCycle() {
        runningCycle = true;
        finished.clear();
        cycleNumber++;
    }


    /**
     *  Updates the number of agents in the MAS, this default
     *  implementation, considers all agents in the MAS as actors .
     */
    public void updateNumberOfAgents() {
        setNbAgs(runtime.getAgentsQty());
    }

    /** Returns the number of agents in the MAS (used to test the end of a cycle) */
    public int getNbAgs() {
        return nbAgs;
    }

    /** Set the number of agents */
    public void setNbAgs(int n) {
        nbAgs = n;
    }

    /**
     * Called when the agent <i>agName</i> has finished its reasoning cycle.
     * <i>breakpoint</i> is true in case the agent selected one plan with "breakpoint"
     * annotation.
      */
    public void receiveFinishedCycle(String agName, boolean breakpoint, int cycle) {
        if (nbAgs < 0 || cycle != this.cycleNumber || finished.size()+1 > nbAgs) {
            updateNumberOfAgents();
        }
        if (cycle == this.cycleNumber && runningCycle) { // the agent finished the current cycle
            lock.lock();
            try {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Agent "+agName+" has finished cycle "+cycle+", # of finished agents is "+(finished.size()+1)+"/"+nbAgs);
                    if (breakpoint) logger.fine("Agent "+agName+" reached a breakpoint");
                }

                finished.add(agName);
                if (testEndCycle(finished)) {
                    agFinishedCond.signal();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Returns true when a new cycle can start, it normally
     * holds when all agents are in the finishedAgs set.
     *
     * @param finishedAgs the set of agents' name that already finished the current cycle
     */
    protected boolean testEndCycle(Set<String> finishedAgs) {
        return finishedAgs.size() >= getNbAgs();
    }

    public void setExecutionControlInfraTier(ExecutionControlInfraTier jasonControl) {
        infraControl = jasonControl;
        runtime = infraControl.getRuntimeServices();
    }

    public ExecutionControlInfraTier getExecutionControlInfraTier() {
        return infraControl;
    }

    /**
     * This method is called when setExecutionControlInfraTier was already called
     */
    public void init(String[] args) {
    }

    /**
     * This method is called when MAS execution is being finished
     */
    public void stop() {
        isRunning = false;
    }

    /** Called when all agents have finished the current cycle */
    protected void allAgsFinished() {
        startNewCycle();
        infraControl.informAllAgsToPerformCycle(cycleNumber);
        logger.fine("starting cycle "+cycleNumber);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public void setRunningCycle(boolean rc) {
        runningCycle = rc;
    }

    @Override
    public String toString() {
        return "Synchronous execution control.";
    }
}
