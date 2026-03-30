package lk.ijse.sensor_service.client;

import lk.ijse.sensor_service.dto.TelemetryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AUTOMATION-SERVICE")
public interface AutomationClient {

    @PostMapping("/api/automation/process")
    String pushToAutomation(@RequestBody TelemetryRequest request);
}