package lk.ijse.zone_service.client;

import lk.ijse.zone_service.dto.DeviceCreateRequest;
import lk.ijse.zone_service.dto.DeviceCreateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "iot-external-api", url = "${external.iot.base-url}")
public interface IoTClient {

    @PostMapping("/auth/login")
    Map<String, Object> login(@RequestBody Map<String, String> loginRequest);

    @PostMapping("/devices")
    DeviceCreateResponse registerDevice(
            @RequestHeader("Authorization") String token,
            @RequestBody DeviceCreateRequest request
    );
}