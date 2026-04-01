package lk.ijse.sensor_service.controller;

import lk.ijse.sensor_service.entity.SensorReading;
import lk.ijse.sensor_service.service.SensorService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * This controller provides endpoints for users (like Farmers) to monitor
 * the environmental conditions (Temperature and Humidity) of the greenhouse.
 */
@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    /**
     * Retrieves a list of all historical sensor readings stored in the database.
     * This allows the farmer to see past weather patterns in the greenhouse.
     */
    @GetMapping
    public List<SensorReading> getAll() {
        return sensorService.getAll();
    }

    /**
     * Retrieves the very last sensor reading captured by the system.
     * This endpoint satisfies the requirement on Page 6 of the assignment PDF
     * for a real-time/debug view of the current environment.
     *
     * @return The latest SensorReading object.
     */
    @GetMapping("/latest")
    public SensorReading getLatest() {
        return sensorService.getLatest();
    }
}