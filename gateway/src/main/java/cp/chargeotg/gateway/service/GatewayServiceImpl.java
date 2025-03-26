package cp.chargeotg.gateway.service;

import cp.chargeotg.dto.AuthorizationCheckEvent;
import cp.chargeotg.dto.ChargingSessionReq;
import cp.chargeotg.dto.ChargingSessionResp;
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

	@Value("${kafka.topic.forward}")
	private String forwardTopic;

	@Value("${kafka.topic.reply}")
	private String replyTopic;

	@Autowired
	private ReplyingKafkaTemplate<String, AuthorizationCheckEvent, ChargingSessionResp> gwReplyingKafkaTemplate;

	@Override
	public ChargingSessionResp createChargingSession(ChargingSessionReq chargingSessionReq) {
		try {
			AuthorizationCheckEvent authorizationCheckEvent = new AuthorizationCheckEvent(chargingSessionReq.stationUuid().toString(), chargingSessionReq.driverId(), chargingSessionReq.callbackUrl().toString());

			gwReplyingKafkaTemplate.start();
			LOGGER.info("Sending {}...", authorizationCheckEvent.getClass());

			ProducerRecord<String, AuthorizationCheckEvent> producerRecord = new ProducerRecord<>(forwardTopic, authorizationCheckEvent);

			producerRecord.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));
			RequestReplyFuture<String, AuthorizationCheckEvent,ChargingSessionResp> requestReplyFutureForChargingSession = gwReplyingKafkaTemplate.sendAndReceive(producerRecord);

			SendResult<String, AuthorizationCheckEvent> result = requestReplyFutureForChargingSession.getSendFuture().get(10, TimeUnit.SECONDS);
			LOGGER.info("The result of sending {} is: {}", authorizationCheckEvent.getClass(), result.getProducerRecord().value().toString());

			ConsumerRecord<String, ChargingSessionResp> consumerRecord = requestReplyFutureForChargingSession.get(10, TimeUnit.SECONDS);
			ChargingSessionResp chargingSessionResp = (ChargingSessionResp) consumerRecord.value();
			LOGGER.info("Received the record {} in exchange.",chargingSessionResp);
			return chargingSessionResp;

		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage());
		} catch (ExecutionException e) {
			LOGGER.error(e.getMessage());
		} catch (TimeoutException e) {
			LOGGER.error(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally
		{
			gwReplyingKafkaTemplate.stop();
		}
		return null;
	}

}
