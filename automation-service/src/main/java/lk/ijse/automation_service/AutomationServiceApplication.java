package lk.ijse.automation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * This is the main starting point for the Automation Service.
 * This service acts as the "Brain" of the greenhouse, making decisions
 * based on the environment data it receives.
 */
@SpringBootApplication
@EnableDiscoveryClient // This allows the service to register itself with the Eureka Discovery Server
@EnableFeignClients    // This enables the Feign client feature, allowing this service to talk to the Zone Service
public class AutomationServiceApplication {

	/**
	 * The standard Java main method that launches the Spring Boot application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(AutomationServiceApplication.class, args);
	}
}