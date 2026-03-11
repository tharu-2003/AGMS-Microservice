package lk.ijse.sensor_service.repository;

import lk.ijse.sensor_service.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
}