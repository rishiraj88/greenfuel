package cp.chargeotg.gateway.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.net.URL;
import java.util.UUID;

public record ChargingSessionReq(UUID stationUuid
        , @Size(min = 20, max = 80, message = "must be 20 and 80 characters")
         @Pattern(regexp="^[\\w\\-\\.~]+$", message="Only alphanumeric characters,  hyphen, period, underscore and tilde are allowed in driver identifier.")
         String driverId
        , URL callbackUrl) {
}
