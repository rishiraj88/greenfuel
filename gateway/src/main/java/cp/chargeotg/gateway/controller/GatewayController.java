package cp.chargeotg.gateway.controller;

import cp.chargeotg.dto.ChargingSessionReq;
import cp.chargeotg.dto.ChargingSessionResp;
import cp.chargeotg.gateway.service.GatewayService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/gateway")
public class GatewayController {
    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChargingSessionResp createChargingSession(@RequestBody @Valid ChargingSessionReq chargingSessionReq) {
        return gatewayService.createChargingSession(chargingSessionReq);
    }
}
