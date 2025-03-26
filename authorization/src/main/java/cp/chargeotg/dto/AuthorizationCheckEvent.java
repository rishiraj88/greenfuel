package cp.chargeotg.dto;

import java.io.Serializable;

public record AuthorizationCheckEvent(String stationId, String driverToken, String callbackUrl) implements Serializable {}