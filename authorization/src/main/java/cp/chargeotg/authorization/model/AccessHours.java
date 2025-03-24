package cp.chargeotg.authorization.model;

import java.time.LocalTime;

public record AccessHours(LocalTime openingAt, LocalTime closingAt) {
}
