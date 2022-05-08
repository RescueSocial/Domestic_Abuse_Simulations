package jason.stdlib;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;


/**
  <p>Internal action: <b><code>.drop_event(<i>D</i>)</code></b>.

  <p>Description: removes events <i>D</i> from the agent circumstance.
  This internal action simply removes all <i>+!D</i> entries
  (those for which <code>.desire(D)</code> would succeed) <i>from the set of events only</i>;
  this action is complementary to <code>.drop_desire</code> and <code>.drop_intention</code>,
  in case a goal is to be removed only from the set of events and <i>not</i> from the set of intentions.
  No event is produced as a consequence of dropping desires from the set of events.

  <p>Parameters:<ul>

  <li>- event (literal): the event to be dropped.</li>

  </ul>

  <p>Example:<ul>

  <li> <code>.drop_event(go(X,Y))</code>: removes events such as
  <code>&lt;+!go(1,3),_&gt;</code> from the set of events.

  </ul>


  @see jason.stdlib.current_intention
  @see jason.stdlib.desire
  @see jason.stdlib.drop_all_desires
  @see jason.stdlib.drop_all_events
  @see jason.stdlib.drop_all_intentions
  @see jason.stdlib.drop_intention
  @see jason.stdlib.drop_desire
  @see jason.stdlib.succeed_goal
  @see jason.stdlib.fail_goal
  @see jason.stdlib.intend


 */
@Manual(
        literal=".drop_event(event)",
        hint="removes referred event from the agent circumstance",
        argsHint= {
                "the event to be dropped"
        },
        argsType= {
                "literal"
        },
        examples= {
                ".drop_event(go(X,Y)): removes events such as &lt;+!go(1,3),_&gt; from the set of events"
        },
        seeAlso= {
                "jason.stdlib.intend",
                "jason.stdlib.desire",
                "jason.stdlib.drop_all_desires",
                "jason.stdlib.drop_all_events",
                "jason.stdlib.drop_all_intentions",
                "jason.stdlib.drop_intention",
                "jason.stdlib.drop_desire",
                "jason.stdlib.succeed_goal",
                "jason.stdlib.fail_goal",
                "jason.stdlib.current_intention",
                "jason.stdlib.resume",
                "jason.stdlib.suspend",
                "jason.stdlib.suspended"
        }
    )
@SuppressWarnings("serial")
public class drop_event extends drop_desire {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        checkArguments(args);
        dropEvt(ts.getC(), (Literal)args[0], un);
        return true;
    }
}
