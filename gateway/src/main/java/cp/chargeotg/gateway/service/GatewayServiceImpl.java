package cp.chargeotg.gateway.service;

import cp.chargeotg.dto.AuthorizationCheckReq;
import cp.chargeotg.dto.AuthorizationCheckResp;
import cp.chargeotg.dto.ChargingSessionReq;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class GatewayServiceImpl implements GatewayService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayServiceImpl.class);
    @Value("${kafka.topic.producer}")
    private String forwardTopic;
    @Value("${kafka.topic.consumer}")
    private String replyTopic;
    @Autowired
    private ReplyingKafkaTemplate<String, AuthorizationCheckReq, AuthorizationCheckResp> gwReplyingKafkaTemplate;

    @Override
    public AuthorizationCheckResp createChargingSession(ChargingSessionReq chargingSessionReq) {
        try {
            var authorizationCheckReq = new AuthorizationCheckReq(chargingSessionReq.stationUuid().toString(), chargingSessionReq.driverId(), chargingSessionReq.callbackUrl().toString());
            gwReplyingKafkaTemplate.start();

            LOGGER.info("Sending {}...", authorizationCheckReq.getClass());
            ProducerRecord<String, AuthorizationCheckReq> producerRecord = new ProducerRecord<>(forwardTopic, authorizationCheckReq);
            producerRecord.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));
            var requestReplyFutureForChargingSession = gwReplyingKafkaTemplate.sendAndReceive(producerRecord);

            // sendResult
            var result = requestReplyFutureForChargingSession.getSendFuture().get(10, TimeUnit.SECONDS);
            LOGGER.info("The result of sending {} is: {}", authorizationCheckReq.getClass(), result.getProducerRecord().value().toString());

            // received record
            var consumerRecord = requestReplyFutureForChargingSession.get(10, TimeUnit.SECONDS);

            // when no timeout happens in obtaining the record
            AuthorizationCheckResp authorizationCheckResp = (AuthorizationCheckResp) consumerRecord.value();
            LOGGER.info("Received the record {} in exchange.", authorizationCheckResp);
            return authorizationCheckResp;

        } catch (TimeoutException e) {
            // when a timeout happens in obtaining the record
            LOGGER.error(e.getMessage());
            AuthorizationCheckResp authorizationCheckResp = new AuthorizationCheckResp(chargingSessionReq.stationUuid().toString(), chargingSessionReq.driverId(), "unknown");
            LOGGER.info("Set the driven token to 'unknown' due to timeout. The updated record is: {}", authorizationCheckResp);
            return authorizationCheckResp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            gwReplyingKafkaTemplate.stop();
        }
        return null;
    }
}
