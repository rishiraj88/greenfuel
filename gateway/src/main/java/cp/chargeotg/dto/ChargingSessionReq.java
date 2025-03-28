package cp.chargeotg.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.net.URL;
import java.util.UUID;

//
public record ChargingSessionReq(UUID stationUuid
        , @Size(min = 20, max = 80, message = "Driver ID must be 20 to 80 characters long.")
         @Pattern(regexp="^[\\w\\-\\.~]+$", message="Only alphanumeric characters,  hyphen, period, underscore and tilde are allowed in driver identifier.")
         String driverId
        , URL callbackUrl) {
}
