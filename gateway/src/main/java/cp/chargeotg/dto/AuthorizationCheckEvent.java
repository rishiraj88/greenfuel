package cp.chargeotg.dto;
public record AuthorizationCheckEvent(String stationId, String driverToken, String callbackUrl){}