package cp.chargeotg.authorization.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor
@Document(value = "driver_authz_status")
public class AuthorizationDecision {
    @Id
    private String id;
    private String stationId;
    private String driverToken;
    private String status;
}
