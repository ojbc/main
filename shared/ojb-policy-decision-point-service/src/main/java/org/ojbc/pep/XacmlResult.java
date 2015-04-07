package org.ojbc.pep;


/**
 * A wrapper of the result of a policy evaluation against a XACML request.
 *
 */
public class XacmlResult {
    
    public static final int DECISION_PERMIT = 0;
    public static final int DECISION_DENY = 1;
    public static final int DECISION_INDETERMINATE = 2;
    public static final int DECISION_NOT_APPLICABLE = 3;
    public static final int DECISION_INDETERMINATE_DENY = 4;
    public static final int DECISION_INDETERMINATE_PERMIT = 5;
    public static final int DECISION_INDETERMINATE_DENY_OR_PERMIT = 6;

    private int decision;
    
    XacmlResult(int decision)
    {
        this.decision = decision;
    }
    
    public int getDecision() {
        return decision;
    }

}
