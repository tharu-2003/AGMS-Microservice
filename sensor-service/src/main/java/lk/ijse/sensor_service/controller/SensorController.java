package lk.ijse.sensor_service.controller;

import lk.ijse.sensor_service.entity.SensorReading;
import lk.ijse.sensor_service.service.SensorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    public List<SensorReading> getAll() {
        return sensorService.getAll();
    }
}