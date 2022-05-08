package jason.stdlib.set;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.SetTermImpl;
import jason.asSyntax.Term;


public class create extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        if (args[0].isVar()) {
            return un.unifies(args[0], new SetTermImpl());
        } else {
            return false;
        }
    }
}
