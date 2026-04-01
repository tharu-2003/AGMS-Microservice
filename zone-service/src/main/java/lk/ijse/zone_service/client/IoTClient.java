package lk.ijse.zone_service.client;

import lk.ijse.zone_service.dto.DeviceCreateRequest;
import lk.ijse.zone_service.dto.DeviceCreateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

/**
 * This interface is a bridge to the External IoT Data Provider.
 * It uses Spring Cloud OpenFeign to register hardware devices for each greenhouse zone.
 * The URL is fetched from the Config Server properties.
 */
@FeignClient(name = "iot-external-api", url = "${external.iot.base-url}")
public interface IoTClient {

    /**
     * Authenticates with the external IoT system to obtain a secure session token.
     * We need this token before we can register any hardware devices.
     *
     * @param loginRequest Map containing external API credentials.
     * @return A Map containing the access token.
     */
    @PostMapping("/auth/login")
    Map<String, Object> login(@RequestBody Map<String, String> loginRequest);

    /**
     * Registers a new virtual hardware sensor in the external IoT system.
     *
     * @param token The Bearer token obtained from the external login method.
     * @param request Contains the device name and the zone ID it belongs to.
     * @return DeviceCreateResponse containing the unique deviceId assigned by the provider.
     */
    @PostMapping("/devices")
    DeviceCreateResponse registerDevice(
            @RequestHeader("Authorization") String token,
            @RequestBody DeviceCreateRequest request
    );
}