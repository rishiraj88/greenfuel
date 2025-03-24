package cp.chargeotg.gateway.service;

import cp.chargeotg.gateway.common.Constants;
import cp.chargeotg.gateway.dto.ChargingSessionReq;
import cp.chargeotg.gateway.dto.ChargingSessionResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@Slf4j
public class GatewayServiceImpl implements GatewayService {
    private final KafkaTemplate<String, AuthorizationCheckEvent> kafkaTemplate;

    public GatewayServiceImpl(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public ChargingSessionResp createChargingSession(ChargingSessionReq chargingSessionReq) {
        /*
        forward to auth service the following, via message queue:
        - station id,
        - driver id,
        - callback url
        */
        log.info("Requesting authorization for driver {} at station {}...", chargingSessionReq.driverId(), chargingSessionReq.stationUuid());

        //use event for invocation of auth service
        var authorizationCheckEvent = new AuthorizationCheckEvent(chargingSessionReq.stationUuid().toString(), chargingSessionReq.driverId(), chargingSessionReq.callbackUrl().toString());
        log.debug("Invoking Authorization Service via message queue...");
        kafkaTemplate.send(Constants.AUTHORIZATION_CHECK_QUEUE_NAME, authorizationCheckEvent);
        log.debug("Invoked Authorization Service via message queue. Authorization Service takes decision about allowing the specific driver to charge at the specific station and sends the decision to callback URL.");
        log.info("Requested authorization for driver {} at station {}.", chargingSessionReq.driverId(), chargingSessionReq.stationUuid());

        return new ChargingSessionResp("accepted", "Request is being processed asynchronously. The result will be sent to the provided callback URL.");         // Auth service sends the decision to callback url.
    }
}
