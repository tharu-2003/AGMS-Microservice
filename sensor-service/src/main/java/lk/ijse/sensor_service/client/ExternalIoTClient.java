package lk.ijse.sensor_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * This interface acts as the "Fetcher" tool for external data.
 * It is responsible for communicating with the Live External IoT Data Provider.
 * The URL is loaded dynamically from the Config Server.
 */
@FeignClient(name = "external-iot-api", url = "${external.iot.base-url}")
public interface ExternalIoTClient {

    /**
     * Authenticates with the external IoT system.
     * We send the 'greenhouse_user' credentials to receive a temporary Access Token.
     *
     * @param credentials Map containing "username" and "password".
     * @return A Map containing the JWT access token from the external system.
     */
    @PostMapping("/auth/login")
    Map<String, Object> login(@RequestBody Map<String, String> credentials);

    /**
     * Fetches real-time environmental data (Temperature and Humidity) for a specific device.
     *
     * @param token The Bearer token obtained from the external login method.
     * @param deviceId The unique ID of the hardware sensor.
     * @return A Map containing the sensor values (Telemetry).
     */
    @GetMapping("/devices/telemetry/{deviceId}")
    Map<String, Object> getTelemetry(
            @RequestHeader("Authorization") String token,
            @PathVariable("deviceId") String deviceId
    );
}