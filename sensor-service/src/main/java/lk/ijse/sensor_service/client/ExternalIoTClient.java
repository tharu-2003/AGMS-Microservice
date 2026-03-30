package lk.ijse.sensor_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@FeignClient(name = "external-iot-api", url = "${external.iot.base-url}")
public interface ExternalIoTClient {

    @PostMapping("/auth/login")
    Map<String, Object> login(@RequestBody Map<String, String> credentials);

    @GetMapping("/devices/telemetry/{deviceId}")
    Map<String, Object> getTelemetry(
            @RequestHeader("Authorization") String token,
            @PathVariable("deviceId") String deviceId
    );
}