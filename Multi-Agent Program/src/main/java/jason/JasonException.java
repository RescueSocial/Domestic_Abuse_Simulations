package jason;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.InternalAction;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;


public class JasonException extends java.lang.Exception {

    private static final long serialVersionUID = 1L;

    public static final Term WRONG_ARGS   = new Atom("wrong_arguments");
    public static final Term UNKNOW_ERROR = new Atom("unknown");

    private static final Term defaultError = new Atom("internal_action");
    private Term error = defaultError;

    private ListTerm errorAnnots = null;

    /**
     * Creates a new instance of <code>JasonException</code> without detail message.
     */
    public JasonException() {
    }

    /**
     * Constructs an instance of <code>JasonException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public JasonException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>JasonException</code> with the specified detail message
     * and error description term.
     *
     * @param msg the detail message.
     * @param error the term that details (in AgentSpeak) the error
     */
    public JasonException(String msg, Term error) {
        super(msg);
        this.error = error;
    }

    public JasonException(String msg, Exception cause) {
        super(msg);
        initCause(cause);
    }

    public JasonException(String msg, Term error, Exception cause) {
        super(msg);
        initCause(cause);
        this.error = error;
    }

    public void addErrorAnnot(Term t) {
        if (errorAnnots == null)
            errorAnnots = new ListTermImpl();
        errorAnnots.append(t);
    }

    public ListTerm getErrorTerms() {
        ListTerm e = createBasicErrorAnnots(error, getMessage());
        if (errorAnnots != null)
            e.concat(errorAnnots.cloneLT());
        return e;
    }

    public static JasonException createWrongArgumentNb(DefaultInternalAction ia) {
        String msg;
        if (ia.getMinArgs() == ia.getMaxArgs())
            if (ia.getMinArgs() == 1)
                msg = " One argument is expected.";
            else
                msg = " "+ia.getMinArgs()+" arguments are expected.";
        else
            msg = " From "+ia.getMinArgs()+" to "+ia.getMaxArgs()+" arguments are expected.";
        return new JasonException("The internal action '"+ia.getClass().getSimpleName()+"' has not received the required number of argument(s)."+msg, WRONG_ARGS);
    }

    public static JasonException createWrongArgument(InternalAction ia, String reason) {
        return new JasonException("Wrong argument for internal action '"+ia.getClass().getSimpleName()+"': "+reason, WRONG_ARGS);
    }

    public static ListTerm createBasicErrorAnnots(String id, String msg) {
        return createBasicErrorAnnots(ASSyntax.createAtom(id), msg);
    }
    public static ListTerm createBasicErrorAnnots(Term id, String msg) {
        return ASSyntax.createList(
                   ASSyntax.createStructure("error", id),
                   ASSyntax.createStructure("error_msg", ASSyntax.createString(msg)));
    }
}
