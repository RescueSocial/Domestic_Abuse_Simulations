package jason.stdlib;

import jason.asSemantics.InternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Term;

/**
  <p>Internal action: <b><code>.intersection(S1,S2,S3)</code></b>.

  <p>Description: S3 is the intersection of the sets S1 and S2 (represented by lists).
  The result set is sorted.

  <p>Parameters:<ul>
  <li>+ arg[0] (a list).<br/>
  <li>+ arg[1] (a list).<br/>
  <li>+/- arg[2]: the result of the intersection.
  </ul>

  <p>Examples:<ul>
  <li> <code>.intersection("[a,b,c]","[b,e]",X)</code>: <code>X</code> unifies with "[b]".
  <li> <code>.intersection("[a,b,a,c]","[f,e,a,c]",X)</code>: <code>X</code> unifies with "[a,c]".
  </ul>

  @see jason.stdlib.concat
  @see jason.stdlib.delete
  @see jason.stdlib.length
  @see jason.stdlib.member
  @see jason.stdlib.sort
  @see jason.stdlib.substring
  @see jason.stdlib.nth
  @see jason.stdlib.max
  @see jason.stdlib.min
  @see jason.stdlib.reverse

  @see jason.stdlib.difference
  @see jason.stdlib.union
*/
@Manual(
        literal=".intersection(arg0,arg1,arg2)",
        hint="results a sorted set of the intersection of two sets",
        argsHint= {
                "the first set",
                "the other set to intersect with the first",
                "the result of the intersection"
        },
        argsType= {
                "list",
                "list",
                "list"
        },
        examples= {
                ".intersection(\"[a,b,c]\",\"[b,e]\",X): X unifies with \"[b]\"",
                ".intersection(\"[a,b,a,c]\",\"[f,e,a,c]\",X): X unifies with \"[a,c]\""
        },
        seeAlso= {
                "jason.stdlib.concat",
                "jason.stdlib.delete",
                "jason.stdlib.length",
                "jason.stdlib.member",
                "jason.stdlib.sort",
                "jason.stdlib.shuffle",
                "jason.stdlib.substring",
                "jason.stdlib.prefix",
                "jason.stdlib.suffix",
                "jason.stdlib.nth",
                "jason.stdlib.max",
                "jason.stdlib.min",
                "jason.stdlib.reverse",
                "jason.stdlib.difference",
                "jason.stdlib.intersection",
                "jason.stdlib.union"
        }
    )
@SuppressWarnings("serial")
public class intersection extends difference {

    // to inherit checkArgs

    private static InternalAction singleton = null;
    public static InternalAction create() {
        if (singleton == null)
            singleton = new intersection();
        return singleton;
    }

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        checkArguments(args);
        return un.unifies(args[2], ((ListTerm)args[0]).intersection( (ListTerm)args[1]) );
    }
}
