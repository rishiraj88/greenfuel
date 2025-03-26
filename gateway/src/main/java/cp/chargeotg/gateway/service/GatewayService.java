package cp.chargeotg.gateway.service;

import cp.chargeotg.dto.ChargingSessionReq;
import cp.chargeotg.dto.ChargingSessionResp;

public interface GatewayService {
    ChargingSessionResp createChargingSession(ChargingSessionReq chargingSessionReq);
}
