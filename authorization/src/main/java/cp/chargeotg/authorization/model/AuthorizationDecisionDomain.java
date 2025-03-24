package cp.chargeotg.authorization.model;

public enum AuthorizationDecisionDomain {
    ALLOWED("allowed"), NOT_ALLOWED("not_allowed"), UNKNOWN("unknown"), INVALID("invalid");

    AuthorizationDecisionDomain(String value) {
    }
}
