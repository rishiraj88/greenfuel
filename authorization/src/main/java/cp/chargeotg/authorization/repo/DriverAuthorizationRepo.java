package cp.chargeotg.authorization.repo;

import cp.chargeotg.authorization.model.AuthorizationDecision;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DriverAuthorizationRepo extends MongoRepository<AuthorizationDecision,String>{}
