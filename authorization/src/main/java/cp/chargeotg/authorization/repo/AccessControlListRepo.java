package cp.chargeotg.authorization.repo;

import cp.chargeotg.authorization.model.AccessControlList;
import cp.chargeotg.authorization.model.DriverGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccessControlListRepo extends MongoRepository<AccessControlList,String>{
    public List<AccessControlList> findAllByDriverGroup(DriverGroup driverGroup);
}
