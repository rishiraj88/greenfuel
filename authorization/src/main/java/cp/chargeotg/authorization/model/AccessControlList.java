package cp.chargeotg.authorization.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(value = "acl")
public class AccessControlList {
    @Id
    private String id;
    private DriverGroup driverGroup;
    private AccessHours accessHours;
}
