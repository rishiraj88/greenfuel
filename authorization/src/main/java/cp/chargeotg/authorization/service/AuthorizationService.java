package cp.chargeotg.authorization.service;
import cp.chargeotg.authorization.model.AuthorizationDecision;
import cp.chargeotg.mq.AuthorizationCheckEvent;
import org.springframework.kafka.annotation.KafkaListener;

public interface AuthorizationService {
    @KafkaListener(topics = "authz-check")
    AuthorizationDecision checkAuthorization(AuthorizationCheckEvent authorizationCheckEvent);
}
