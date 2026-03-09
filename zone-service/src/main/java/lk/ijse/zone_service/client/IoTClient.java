package lk.ijse.zone_service.client;

import lk.ijse.zone_service.dto.DeviceCreateRequest;
import lk.ijse.zone_service.dto.DeviceCreateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IoTClient {

    @Value("${external.iot.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public DeviceCreateResponse registerDevice(String accessToken, DeviceCreateRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<DeviceCreateRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<DeviceCreateResponse> response = restTemplate.exchange(
                baseUrl + "/devices",
                HttpMethod.POST,
                entity,
                DeviceCreateResponse.class
        );

        return response.getBody();
    }
}