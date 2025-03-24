package cp.chargeotg.authorization.service;

import cp.chargeotg.authorization.model.AccessControlList;
import cp.chargeotg.authorization.model.AuthorizationDecision;
import cp.chargeotg.authorization.model.AuthorizationDecisionDomain;
import cp.chargeotg.authorization.model.DriverGroup;
import cp.chargeotg.authorization.repo.AccessControlListRepo;
import cp.chargeotg.mq.AuthorizationCheckEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    private final AccessControlListRepo accessControlListRepo;

    @Override
    @KafkaListener(topics = "authz-check")
    public AuthorizationDecision checkAuthorization(AuthorizationCheckEvent authorizationCheckEvent) {
        log.info("Got a message out of event: {}", authorizationCheckEvent);

        AuthorizationDecisionDomain status = AuthorizationDecisionDomain.UNKNOWN; // default value, and for timeout as well
        DriverGroup driverGroup = getDriverGroup(authorizationCheckEvent.getDriverToken().toString());
        List<AccessControlList> acLists = accessControlListRepo.findAllByDriverGroup(driverGroup);
        if (null == acLists || acLists.isEmpty()) {
            status = AuthorizationDecisionDomain.INVALID;
        } else if (driverGroup.equals(DriverGroup.EMPLOYEE) && acLists.stream().findFirst().get().getAccessHours().closingAt().compareTo(acLists.stream().findFirst().get().getAccessHours().openingAt()) > 0) {
            status = AuthorizationDecisionDomain.ALLOWED;
        } else if (driverGroup.equals(DriverGroup.OTHERS) && acLists.stream().findFirst().get().getAccessHours().closingAt().compareTo(acLists.stream().findFirst().get().getAccessHours().openingAt()) < 0) {
            status = AuthorizationDecisionDomain.ALLOWED;
        } else status = AuthorizationDecisionDomain.NOT_ALLOWED;

        AuthorizationDecision decision = new AuthorizationDecision();
        decision.setStationId(authorizationCheckEvent.getStationId().toString());
        decision.setDriverToken(authorizationCheckEvent.getDriverToken().toString());
        decision.setStatus(status.toString());

        //send this decision to callback URL

        return null;// authorizationRepo.save(decision);
    }

    private DriverGroup getDriverGroup(String driverId) {
        return (driverId.startsWith("CPCP")) ? DriverGroup.EMPLOYEE : DriverGroup.OTHERS;
    }
}