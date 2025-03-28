package cp.chargeotg.authorization.service;

import cp.chargeotg.authorization.model.AccessControlList;
import cp.chargeotg.authorization.model.AuthorizationDecision;
import cp.chargeotg.authorization.model.DriverAuthorizationStatus;
import cp.chargeotg.authorization.model.DriverGroup;
import cp.chargeotg.authorization.repo.AccessControlListRepo;
import cp.chargeotg.authorization.repo.DriverAuthorizationRepo;
import cp.chargeotg.dto.AuthorizationCheckReq;
import cp.chargeotg.dto.AuthorizationCheckResp;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationServiceImpl.class);
    private final AccessControlListRepo accessControlListRepo;
    private final DriverAuthorizationRepo driverAuthorizationRepo;

    public AuthorizationServiceImpl(AccessControlListRepo accessControlListRepo, DriverAuthorizationRepo driverAuthorizationRepo) {
        this.accessControlListRepo = accessControlListRepo;
        this.driverAuthorizationRepo = driverAuthorizationRepo;
    }

    @KafkaListener(topics = "${kafka.topic.consumer}")
    @SendTo("${kafka.topic.producer}")
    @Override
    public AuthorizationCheckResp checkAuthorization(ConsumerRecord<String, AuthorizationCheckReq> consumerRecord, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId) {
        LOGGER.info("Received {} for key {} on topic {}", consumerRecord.value().toString(), consumerRecord.key(), consumerRecord.topic());
        consumerRecord.headers().add(KafkaHeaders.CORRELATION_ID, correlationId);
        AuthorizationCheckReq authorizationCheckReq = consumerRecord.value();

        AuthorizationDecision decision = new AuthorizationDecision();
        decision.setStationId(authorizationCheckReq.stationId());
        decision.setDriverToken(authorizationCheckReq.driverToken());
        decision.setStatus(driverAuthDecision(authorizationCheckReq.driverToken()));
        driverAuthorizationRepo.save(decision);

        return new AuthorizationCheckResp(authorizationCheckReq.stationId(),authorizationCheckReq.driverToken(),decision.getStatus());
    }

    private String driverAuthDecision(String driverToken) {
        DriverAuthorizationStatus status = DriverAuthorizationStatus.UNKNOWN; // default value, and for timeout as well
        DriverGroup driverGroup = getDriverGroup(driverToken);
        List<AccessControlList> acLists = accessControlListRepo.findAllByDriverGroup(driverGroup);
        if (null == acLists || acLists.isEmpty()) {
            status = DriverAuthorizationStatus.INVALID;
        } else if (driverGroup.equals(DriverGroup.EMPLOYEE) && acLists.stream().findFirst().get().getAccessHours().closingAt().isAfter(acLists.stream().findFirst().get().getAccessHours().openingAt())) {
            status = DriverAuthorizationStatus.ALLOWED;
        } else if (driverGroup.equals(DriverGroup.OTHERS) && acLists.stream().findFirst().get().getAccessHours().closingAt().isBefore(acLists.stream().findFirst().get().getAccessHours().openingAt())) {
            status = DriverAuthorizationStatus.ALLOWED;
        } else status = DriverAuthorizationStatus.NOT_ALLOWED;

        return status.toString();
    }
    private DriverGroup getDriverGroup(java.lang.String driverId) {
        return (driverId.startsWith("CPCP")) ? DriverGroup.EMPLOYEE : DriverGroup.OTHERS;
    }
}
