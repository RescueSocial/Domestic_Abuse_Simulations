package jason.stdlib;

import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import jason.bb.BeliefBase;

import java.util.List;

/**
  <p>Internal action: <b><code>.asserta</code></b>.

  <p>Description: adds a new belief as the "+" (or "+<") operator. However, it can be used in prolog like rules.

  <p>Parameters:<ul>
  <li>+ belief (literal): the belief that will be added at the beginning of the belief base.<br/>
  </ul>

  <p>Examples:<ul>
  <li> <code>.asserta(p)</code>: adds <code>p</code> at the beggining of the belief base.</li>
  </ul>

  @see jason.stdlib.assertz
  @see jason.stdlib.abolish

 */
@Manual(
        literal=".asserta(belief)",
        hint="adds a new belief using prolog like rules",
        argsHint= {
                "the belief that will be added at the beginning of the base"
        },
        argsType= {
                "literal"
        },
        examples= {
                ".asserta(p): adds p at the beginning of the belief base"
        },
        seeAlso= {
                "jason.stdlib.assertz",
                "jason.stdlib.abolish"
        }
    )
@SuppressWarnings("serial")
public class asserta extends DefaultInternalAction {

    @Override public int getMinArgs() {
        return 1;
    }
    @Override public int getMaxArgs() {
        return 1;
    }

    @Override protected void checkArguments(Term[] args) throws JasonException {
        super.checkArguments(args); // check number of arguments
        if (!args[0].isLiteral())
            if (!args[0].isGround() && !args[0].isRule())
                throw JasonException.createWrongArgument(this,"first argument must be a ground literal (or rule).");
    }

    @Override public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        checkArguments(args);
        Literal l = (Literal)args[0];
        if (!l.hasSource())
            l.addAnnot(BeliefBase.TSelf);
        List<Literal>[] result = ts.getAg().brf(l,null,null,false);
        if (result != null) { // really added something
            // generate events
            ts.updateEvents(result,null);
        }
        return true;
    }

}
