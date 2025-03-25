package cp.chargeotg.common;
public record AuthorizationCheckEvent(String stationId, String driverToken, String callbackUrl){}