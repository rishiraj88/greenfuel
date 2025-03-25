package cp.chargeotg.gateway.service;

import cp.chargeotg.common.AuthorizationCheckEvent;
import cp.chargeotg.common.Constants;
import cp.chargeotg.gateway.dto.ChargingSessionReq;
import cp.chargeotg.gateway.dto.ChargingSessionResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class GatewayServiceImpl implements GatewayService {
    @Autowired
    private KafkaTemplate<String, AuthorizationCheckEvent> kafkaTemplate;

    @Override
    public ChargingSessionResp createChargingSession(ChargingSessionReq chargingSessionReq) {
        /*
        forward to auth service the following, via message queue:
        - station id,
        - driver id,
        - callback url
        */
        //log.info("Requesting authorization for driver {} at station {}...", chargingSessionReq.driverId(), chargingSessionReq.stationUuid());

        //use event for invocation of auth service
        var authorizationCheckEvent = new AuthorizationCheckEvent(chargingSessionReq.stationUuid().toString(), chargingSessionReq.driverId(), chargingSessionReq.callbackUrl().toString());
        //log.debug("Invoking Authorization Service via message queue...");
        CompletableFuture<SendResult<String, AuthorizationCheckEvent>> task = kafkaTemplate.send(Constants.AUTHORIZATION_CHECK_QUEUE_NAME, authorizationCheckEvent);
        //task.whenComplete()
        //log.debug("Invoked Authorization Service via message queue. Authorization Service takes decision about allowing the specific driver to charge at the specific station and sends the decision to callback URL.");
        //log.info("Requested authorization for driver {} at station {}.", chargingSessionReq.driverId(), chargingSessionReq.stationUuid());

        // Auth service sends the decision to callback url. Gateway will not take care of that.
        return new ChargingSessionResp("accepted", "Request is being processed asynchronously. The result will be sent to the provided callback URL.");
    }
}
