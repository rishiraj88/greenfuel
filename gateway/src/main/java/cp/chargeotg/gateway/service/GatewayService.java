package cp.chargeotg.gateway.service;

import cp.chargeotg.gateway.dto.ChargingSessionReq;
import cp.chargeotg.gateway.dto.ChargingSessionResp;

public interface GatewayService {
    ChargingSessionResp createChargingSession(ChargingSessionReq chargingSessionReq);
}
