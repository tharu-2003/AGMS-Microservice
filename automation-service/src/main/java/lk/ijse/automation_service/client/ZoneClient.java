package lk.ijse.automation_service.client;

import lk.ijse.automation_service.dto.ZoneResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * This interface acts as a communicator between the Automation Service and the Zone Service.
 * It uses Spring Cloud OpenFeign to talk to another microservice without writing complex HTTP code.
 */
@FeignClient(name = "ZONE-SERVICE")
public interface ZoneClient {

    /**
     * This method sends a request to the Zone Service to get details about a specific zone.
     * We need this to know the min and max temperature limits set by the farmer.
     *
     * @param id The unique ID of the zone.
     * @return ZoneResponse containing the name and temperature thresholds.
     */
    @GetMapping("/api/zones/{id}")
    ZoneResponse getZone(@PathVariable("id") Long id);
}