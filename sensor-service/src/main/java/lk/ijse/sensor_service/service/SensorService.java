package lk.ijse.sensor_service.service;

import lk.ijse.sensor_service.dto.TelemetryRequest;
import lk.ijse.sensor_service.entity.SensorReading;
import lk.ijse.sensor_service.repository.SensorReadingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class SensorService {

    private final SensorReadingRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();

    public SensorService(SensorReadingRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 10000)
    public void generateAndSendTelemetry() {
        SensorReading reading = new SensorReading();
        reading.setZoneId("5");         //for testing
        reading.setTemperature(20 + (40 - 20) * random.nextDouble());
        reading.setHumidity(50 + (80 - 50) * random.nextDouble());
        reading.setCapturedAt(LocalDateTime.now().toString());

        SensorReading saved = repository.save(reading);

        TelemetryRequest request = new TelemetryRequest();
        request.setZoneId(saved.getZoneId());
        request.setTemperature(saved.getTemperature());
        request.setHumidity(saved.getHumidity());

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://localhost:8083/api/automation/process",
                    request,
                    String.class
            );

            System.out.println("Automation response: " + response.getBody());
            System.out.println("Sent telemetry -> zoneId=" + request.getZoneId()
                    + ", temp=" + request.getTemperature());

        } catch (Exception e) {
            System.out.println("Automation service call failed: " + e.getMessage());
        }
    }

    public List<SensorReading> getAll() {
        return repository.findAll();
    }
}