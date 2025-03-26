package cp.chargeotg.dto;

import java.io.Serializable;

public record ChargingSessionResp(String status, String message) implements Serializable {}
