package lk.ijse.sensor_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * This is the main entry point for the Sensor Telemetry Service.
 * This service acts as the 'Data Bridge' that connects the greenhouse
 * hardware to our software system.
 */
@SpringBootApplication
@EnableDiscoveryClient // Allows this service to register with Eureka so the Gateway can find it
@EnableScheduling      // Enables the background task that fetches sensor data every 10 seconds
@EnableFeignClients    // Enables OpenFeign to communicate with the External IoT API and Automation Service
public class SensorServiceApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(SensorServiceApplication.class, args);
	}

}