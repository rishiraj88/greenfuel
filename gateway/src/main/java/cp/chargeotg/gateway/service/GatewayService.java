package cp.chargeotg.gateway.service;

import cp.chargeotg.dto.AuthorizationCheckResp;
import cp.chargeotg.dto.ChargingSessionReq;

public interface GatewayService {
    AuthorizationCheckResp createChargingSession(ChargingSessionReq chargingSessionReq);
}
