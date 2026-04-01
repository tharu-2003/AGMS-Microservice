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

/**
 * This service is the "Engine" of the system.
 * It automatically gathers data from the outside world and feeds it into
 * our internal greenhouse automation logic.
 */
@Service
public class SensorService {

    private final SensorReadingRepository repository;
    private final ExternalIoTClient externalIoTClient;
    private final AutomationClient automationClient;

    // Stores the session token for the external IoT API to avoid logging in every 10 seconds
    private String cachedExternalToken = null;

    public SensorService(SensorReadingRepository repository,
                         ExternalIoTClient externalIoTClient,
                         AutomationClient automationClient) {
        this.repository = repository;
        this.externalIoTClient = externalIoTClient;
        this.automationClient = automationClient;
    }

    /**
     * THE DATA BRIDGE: This method is scheduled to run every 10 seconds.
     * It handles the complete lifecycle: Fetch -> Simulate (if needed) -> Save -> Push.
     */
    @Scheduled(fixedRate = 10000)
    public void fetchAndPushTelemetry() {
        Double temp;
        Double humidity;
        String zoneId = "1"; // Assigned to Zone 1 for this implementation

        try {
            // STEP 1: AUTHENTICATION WITH EXTERNAL PROVIDER
            // We check if we already have a valid token; if not, we log in.
            if (cachedExternalToken == null) {
                Map<String, String> creds = Map.of("username", "greenhouse_user", "password", "123456");
                Map<String, Object> loginRes = externalIoTClient.login(creds);
                cachedExternalToken = "Bearer " + loginRes.get("accessToken").toString();
            }

            // STEP 2: FETCH REAL TELEMETRY
            // Request the latest environmental data for our specific device ID.
            String deviceId = "b751b8c9-644a-484c-ba3f-be63f9b27ad0";
            Map<String, Object> telemetry = externalIoTClient.getTelemetry(cachedExternalToken, deviceId);

            // Extract values from the JSON response
            Map<String, Object> values = (Map<String, Object>) telemetry.get("value");
            temp = Double.valueOf(values.get("temperature").toString());
            humidity = Double.valueOf(values.get("humidity").toString());

            System.out.println(">>> SUCCESS: Real data fetched from External IoT API");

        } catch (Exception e) {
            // STEP 3: FALLBACK (SIMULATION MODE)
            // If the external provider is offline or unreachable, we generate
            // random sensor data so the Automation Service can still be tested.
            cachedExternalToken = null;
            System.err.println("!!! FALLBACK: External API unreachable. Generating simulated data.");

            temp = 20.0 + (Math.random() * 20.0);    // Random temperature between 20-40°C
            humidity = 50.0 + (Math.random() * 30.0); // Random humidity between 50-80%
        }

        try {
            // STEP 4: PERSISTENCE (SAVE TO DATABASE)
            // We save every reading to keep a historical record for the farmer.
            SensorReading reading = new SensorReading();
            reading.setZoneId(zoneId);
            reading.setTemperature(temp);
            reading.setHumidity(humidity);
            reading.setCapturedAt(LocalDateTime.now().toString());
            repository.save(reading);

            // STEP 5: PUSH TO AUTOMATION SERVICE
            // We send the current environment data to the "Brain" (Automation Service)
            // which will decide if the Fan or Heater needs to be turned ON.
            TelemetryRequest pushReq = new TelemetryRequest();
            pushReq.setZoneId(zoneId);
            pushReq.setTemperature(temp);
            pushReq.setHumidity(humidity);

            String actionTaken = automationClient.pushToAutomation(pushReq);
            System.out.println(">>> STATUS UPDATE: Temp=" + String.format("%.2f", temp) + "C | Action=" + actionTaken);

        } catch (Exception ex) {
            // If our internal Automation Service is down, we log the error here.
            System.err.println("!!! PUSH ERROR: Failed to communicate with Automation Service.");
        }
    }

    /**
     * Returns a list of all historical sensor data.
     */
    public List<SensorReading> getAll() {
        return repository.findAll();
    }

    /**
     * Finds and returns the very last reading captured by the system.
     */
    public SensorReading getLatest() {
        return repository.findFirstByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No telemetry data found in database."));
    }
}