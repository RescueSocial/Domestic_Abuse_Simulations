package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.InternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

import java.util.logging.Level;


/**
  <p>Internal action: <b><code>.println</code></b>.

  <p>Description: used for printing messages to the console. Exactly as for
  <code>.print</code> except that a new line is printed after the parameters.


  @see jason.stdlib.print
  @see jason.stdlib.printf

*/
@Manual(
        literal=".println(arg0[,arg1,...])",
        hint="used for printing messages to the console such as .print except that a new line is printed after the parameters",
        argsHint= {
                "the term to be printed out",
                "the term to be concatenated with prior ones and printed out [optional]",
        },
        argsType= {
                "term",
                "term"
        },
        examples= {
                ".println(1,X,\"bla\"): prints out the concatenation of the string representations of the number 1, content of variable X, and the string \"bla\""
        },
        seeAlso= {
                "jason.stdlib.print",
                "jason.stdlib.printf"
        }
    )
@SuppressWarnings("serial")
public class println extends DefaultInternalAction {

    private static InternalAction singleton = null;
    public static InternalAction create() {
        if (singleton == null)
            singleton = new println();
        return singleton;
    }

    protected String getNewLine() {
        return "\n";
    }

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        String sout = argsToString(args);

        if (ts != null && ts.getSettings().logLevel() != Level.WARNING) {
            ts.getLogger().info(sout.toString());
        } else {
            System.out.print(sout.toString() + getNewLine());
        }

        return true;
    }

    protected String argsToString(Term[] args) {
        StringBuilder sout = new StringBuilder();
        //try {
        //    if (ts.getSettings().logLevel() != Level.WARNING && args.length > 0) {
        //        sout = new StringBuilder();
        //    }
        //} catch (Exception e) {}

        for (int i = 0; i < args.length; i++) {
            if (args[i].isString()) {
                StringTerm st = (StringTerm)args[i];
                sout.append(st.getString());
            } else {
                Term t = args[i];
                if (! t.isVar()) {
                    sout.append(t);
                } else {
                    sout.append(t+"<no-value>");
                }
            }
        }
        return sout.toString();
    }
}
