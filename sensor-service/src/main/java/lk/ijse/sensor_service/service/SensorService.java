package lk.ijse.sensor_service.service;

import lk.ijse.sensor_service.client.AutomationClient;
import lk.ijse.sensor_service.client.ExternalIoTClient;
import lk.ijse.sensor_service.dto.TelemetryRequest;
import lk.ijse.sensor_service.entity.SensorReading;
import lk.ijse.sensor_service.repository.SensorReadingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class SensorService {

    private final SensorReadingRepository repository;
    private final ExternalIoTClient externalIoTClient;
    private final AutomationClient automationClient;

    private String cachedExternalToken = null;

    public SensorService(SensorReadingRepository repository,
                         ExternalIoTClient externalIoTClient,
                         AutomationClient automationClient) {
        this.repository = repository;
        this.externalIoTClient = externalIoTClient;
        this.automationClient = automationClient;
    }

    @Scheduled(fixedRate = 10000)
    public void fetchAndPushTelemetry() {
        Double temp;
        Double humidity;
        String zoneId = "1";

        try {
            // 1. බාහිර API එකෙන් දත්ත ගැනීමට උත්සාහ කිරීම
            if (cachedExternalToken == null) {
                Map<String, String> creds = Map.of("username", "greenhouse_user", "password", "123456");
                Map<String, Object> loginRes = externalIoTClient.login(creds);
                cachedExternalToken = "Bearer " + loginRes.get("accessToken").toString();
            }

            String deviceId = "b751b8c9-644a-484c-ba3f-be63f9b27ad0";
            Map<String, Object> telemetry = externalIoTClient.getTelemetry(cachedExternalToken, deviceId);

            Map<String, Object> values = (Map<String, Object>) telemetry.get("value");
            temp = Double.valueOf(values.get("temperature").toString());
            humidity = Double.valueOf(values.get("humidity").toString());

            System.out.println(">>> DATA FETCHED FROM EXTERNAL API");

        } catch (Exception e) {
            // 2. FALLBACK: බාහිර API එක වැඩ නැතිනම් දත්ත අනුකරණය (Simulation) කිරීම
            cachedExternalToken = null;
            System.err.println("!!! EXTERNAL API OFFLINE: Using Simulated Data for Testing !!!");

            // අහඹු ලෙස (Random) දත්ත ජනනය කිරීම (Automation එක පරීක්ෂා කිරීමට)
            temp = 20.0 + (Math.random() * 20.0); // 20 - 40 අතර
            humidity = 50.0 + (Math.random() * 30.0); // 50 - 80 අතර
        }

        // 3. පොදු කොටස: Database එකේ Save කිරීම සහ Automation එකට Push කිරීම
        try {
            SensorReading reading = new SensorReading();
            reading.setZoneId(zoneId);
            reading.setTemperature(temp);
            reading.setHumidity(humidity);
            reading.setCapturedAt(LocalDateTime.now().toString());
            repository.save(reading);

            TelemetryRequest pushReq = new TelemetryRequest();
            pushReq.setZoneId(zoneId);
            pushReq.setTemperature(temp);
            pushReq.setHumidity(humidity);

            String actionTaken = automationClient.pushToAutomation(pushReq);
            System.out.println(">>> CURRENT STATUS: Temp=" + String.format("%.2f", temp) + "C | Action: " + actionTaken);

        } catch (Exception ex) {
            System.err.println("!!! AUTOMATION PUSH FAILED: " + ex.getMessage());
        }
    }

    public List<SensorReading> getAll() {
        return repository.findAll();
    }

    public SensorReading getLatest() {
        return repository.findFirstByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No telemetry data available yet."));
    }
}