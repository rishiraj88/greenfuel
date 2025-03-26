package cp.chargeotg.gateway.clients;

import cp.chargeotg.dto.AuthorizationCheckResp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface AutodashClient {
    @PostExchange("/api/v1/drivers")
    public void processChargingSessionAuthorizationStatusForDriver(@RequestBody AuthorizationCheckResp authorizationCheckResp);
}
