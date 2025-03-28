package cp.chargeotg.dto;

import java.io.Serializable;

public record AuthorizationCheckReq(String stationId, String driverToken, String callbackUrl) implements Serializable {}
