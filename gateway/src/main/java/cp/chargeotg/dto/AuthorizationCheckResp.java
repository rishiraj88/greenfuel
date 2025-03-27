package cp.chargeotg.dto;

import java.io.Serializable;

public record AuthorizationCheckResp(String stationId, String driverToken, String status) implements Serializable {}
