package cp.chargeotg.authorization.model;


public enum DriverAuthorizationStatus { ALLOWED("allowed"),NOT_ALLOWED("not_allowed"),UNKNOWN("unknown"),INVALID("invalid");

    DriverAuthorizationStatus(String allowed) {
    }
}