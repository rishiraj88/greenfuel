package cp.chargeotg.autodash.controller;

import cp.chargeotg.dto.AuthorizationCheckResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/api/v1/drivers")
public class AutodashController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutodashController.class);

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void processChargingSessionAuthorizationStatusForDriver(@RequestBody AuthorizationCheckResp authorizationCheckResp) {
        LOGGER.info("Received the status for charging session: {}",authorizationCheckResp.status());
    }
}
