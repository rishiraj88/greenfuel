package cp.chargeotg.mq;
public record AuthorizationCheckEvent(String stationId, String driverToken, String callbackUrl){}