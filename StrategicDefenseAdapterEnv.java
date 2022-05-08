import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;

import org.jpl7.Util;
import org.jpl7.Compound;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Variable;

public class StrategicDefenseAdapterEnv extends Environment {

    Literal init0  = Literal.parseLiteral("continue");
    Literal init1  = Literal.parseLiteral("do(husband,physicallyAttack(husband,wife))");
    Literal init2  = Literal.parseLiteral("do(wife,nothing)");

    @Override
    public void init(String[] args) {
    Query.oneSolution("consult('jason_strategic_defense_wrapper.pl')");
        // initial percepts
        // addPercept(init0);
        // addPercept(init1);
        // addPercept(init2);
    }

    /**
     * Implementation of the agent's basic actions
     */
    @Override
    public boolean executeAction(String ag, Structure act) {
        System.out.println("Agent "+ag+" is doing "+act);

        clearPercepts();

    try {
        var string = "jpl_execute_action("+ag+","+act+",CurrentEvents,Result)";
        System.out.println("  " + string);
        var solution =  Query.oneSolution(string);
        var currentEvents = solution.get("CurrentEvents");
        if (currentEvents.isVariable()) {
        System.out.println("\t" + ag + " has no currentEvents");
        // getEnvironmentInfraTier().getRuntimeServices().stopMAS();
        } else {
        // /System.out.println("\t" + ag + " currentEvents: " + currentEvents);
        Term[] arr = Util.listToTermArray(currentEvents);
        int i;
        for (i = 0; i < arr.length; ++i) {
            Term oneTerm = arr[i];
            System.out.println("\t" + ag + " <<<" + oneTerm.toString() + ">>>");
            addPercept(Literal.parseLiteral(oneTerm.toString()));
        }
        var result = solution.get("Result");
        System.out.println("\t" + ag + " Result " + result.toString());
        } 
        // addPercept(init0);
        System.out.println("");
        informAgsEnvironmentChanged();

        return true;
    } catch (Exception e) {
            System.out.println("error executing " + ag + " for " + act + ": " + e.toString());
        // throw e;
        return false;
    }
    }
}
