package cp.chargeotg.gateway.controller;

import cp.chargeotg.gateway.dto.ChargingSessionReq;
import cp.chargeotg.gateway.dto.ChargingSessionResp;
import cp.chargeotg.gateway.service.GatewayServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class GatewayControllerTest {
    private static ChargingSessionReq chargingSessionReq;
    private static URL callbackUrl;
    private static Validator validator;
    @InjectMocks
    private GatewayController gatewayController;
    @Mock
    private GatewayServiceImpl gatewayService;

    @BeforeAll
    static void beforeAll() throws URISyntaxException, MalformedURLException {
        callbackUrl = URL.of(new URI("http://localhost:8080/api/v1/gateway"), null);
        chargingSessionReq = new ChargingSessionReq(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "validDriverToken123validDriverToken123", callbackUrl);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {//this.gatewayController = gatewayController;
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void should_return_violations_when_driverId_is_shorter_than_20_characters() {
        //when
        chargingSessionReq = new ChargingSessionReq(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "0123456789shortId19", callbackUrl);
        //then
        Set<ConstraintViolation<ChargingSessionReq>> violations = validator.validate(chargingSessionReq);
        System.out.println(violations);
        assertNotEquals(violations.size(), 0);
    }

    @Test
    void should_return_violations_when_driverId_is_longer_than_80_characters() {
        //when
        chargingSessionReq = new ChargingSessionReq(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "0123456789".repeat(8) + "1", callbackUrl);
        //then
        Set<ConstraintViolation<ChargingSessionReq>> violations = validator.validate(chargingSessionReq);
        System.out.println(violations);
        assertNotEquals(violations.size(), 0);
    }

    @Test
    void should_return_violations_when_driverId_contains_invalid_characters() {
        //when
        chargingSessionReq = new ChargingSessionReq(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "0123456789:invalidId19", callbackUrl);
        //then
        Set<ConstraintViolation<ChargingSessionReq>> violations = validator.validate(chargingSessionReq);
        assertNotEquals(violations.size(), 0);
    }

    @Test
    void should_be_valid_when_driverId_contains_valid_characters_and_is_adequately_long() {
        //given
        String driverId = "validDriverToken123validDriverToken123";
        //when
        chargingSessionReq = new ChargingSessionReq(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), driverId, callbackUrl);
        //then
        Set<ConstraintViolation<ChargingSessionReq>> violations = validator.validate(chargingSessionReq);
        assertEquals(violations.size(), 0);
    }

    @Test
    void should_return_violations_when_callbackUrl_is_not_a_valid_URL() {
        //given
        //URI rule:
        //https://www.rfc-editor.org/rfc/rfc3986#section-2
        String badString = "httpÖ";
        //when-then
        assertThrows(Exception.class, () -> callbackUrl = URL.of(new URI(badString), null));
    }

    @Test
    void should_be_valid_when_URL_string_is_a_valid_URI() throws MalformedURLException {
        //given
        String niceString = "https://www.google.com/search?q=sdfsdfasdf&newwindow=1";
        //when
        chargingSessionReq = new ChargingSessionReq(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "validDriverToken123validDriverToken123", new URL(niceString));
        //then
        assertNotNull(chargingSessionReq);
    }

   // @Test
    void should_return_chargingSessionResponse() {
        //given
        ChargingSessionResp expectedChargingSessionResp = new ChargingSessionResp("accepted", "Request is being processed asynchronously. The result will be sent to the provided callback URL.");
        //when
        Mockito.when(gatewayService.createChargingSession(chargingSessionReq)).thenReturn(new ChargingSessionResp("accepted", "Request is being processed asynchronously. The result will be sent to the provided callback URL."));
        //then-return
        ChargingSessionResp actualChargingSessionResp = gatewayController.createChargingSession(chargingSessionReq);
        assertEquals(expectedChargingSessionResp.message(), actualChargingSessionResp.message());
        assertEquals(expectedChargingSessionResp.status(), actualChargingSessionResp.status());
    }
}