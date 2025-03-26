package cp.chargeotg.authorization.service;

import cp.chargeotg.authorization.model.AuthorizationDecision;
import cp.chargeotg.dto.AuthorizationCheckReq;
import cp.chargeotg.dto.AuthorizationCheckResp;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface AuthorizationService{
    public AuthorizationCheckResp checkAuthorization(ConsumerRecord<String, AuthorizationCheckReq> record, byte[] correlationId);

}
