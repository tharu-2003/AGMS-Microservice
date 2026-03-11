package lk.ijse.automation_service.client;

import lk.ijse.automation_service.dto.ZoneResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ZONE-SERVICE")
public interface ZoneClient {

    @GetMapping("/api/zones/{id}")
    ZoneResponse getZone(@PathVariable("id") Long id);
}