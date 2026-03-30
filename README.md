
-----

# 🌿 Automated Greenhouse Management System (AGMS)

### *Cloud-Native Microservice Architecture for Precision Agriculture*

The **Automated Greenhouse Management System (AGMS)** is a robust, microservice-based platform designed to optimize crop yield and resource efficiency. By integrating with live IoT data providers, the system monitors environmental telemetry (Temperature & Humidity) and utilizes a custom Rule Engine to trigger automated climate control actions.

-----

## 🌟 Key Features

* **Centralized Security:** JWT-based authorization enforced at the **API Gateway** level.
* **Dynamic Service Discovery:** Utilizes **Netflix Eureka** for seamless service registration.
* **External Configuration:** **Spring Cloud Config** for centralized property management.
* **State Machine Logic:** Sophisticated lifecycle management for crops (*Seedling -\> Vegetative -\> Harvested*).
* **Automated Rule Engine:** Real-time decision-making based on zone-specific environmental thresholds.
* **IoT Integration:** Scheduled data fetching from External Reactive WebFlux APIs with built-in fallback simulation.

-----

## 🛠 Tech Stack

| Component | Technology |
| :--- | :--- |
| **Backend** | Java 17, Spring Boot 3.x, Spring Cloud |
| **Security** | Spring Security, JWT (JSON Web Token) |
| **Communication** | OpenFeign (Synchronous), RESTful HTTP |
| **Database** | MySQL (Database-per-service pattern) |
| **Service Discovery** | Netflix Eureka |
| **Config Management** | Spring Cloud Config Server |
| **API Gateway** | Spring Cloud Gateway |

-----

## 🏛 System Architecture & Port Mapping

| Service Name | Port | Description |
| :--- | :--- | :--- |
| **Config Server** | `8888` | Centralized configuration via Git/Local Repo |
| **Discovery Server** | `8761` | Netflix Eureka Service Registry |
| **API Gateway** | `8080` | Single entry point with JWT Auth Filter |
| **Auth Service** | `8085` | Identity management, Login, & Refresh Tokens |
| **Zone Service** | `8081` | Greenhouse sections & environmental limits |
| **Sensor Service** | `8082` | IoT Data Bridge (Fetcher/Pusher) |
| **Automation Service**| `8083` | Rule Engine & Action Logging |
| **Crop Service** | `8084` | Inventory & Growth State Machine |

-----

## 🚀 Getting Started (Startup Order)

To ensure the system initializes correctly, follow this sequence:

1.  **MySQL Database:**
    * Ensure MySQL is running.
    * Create the following databases: `agms_auth_db`, `agms_zone_db`, `agms_sensor_db`, `agms_automation_db`, `agms_crop_db`.
2.  **Config Server (8888):** Start first to serve configurations.
3.  **Discovery Server (8761):** Start to enable service registration.
4.  **Auth Service (8085) & API Gateway (8080):** Start to enable security and routing.
5.  **Domain Services (8081-8084):** Start these in any order.

-----

## 🔑 Authentication & Testing

### 1\. External IoT Login (System Use Only)

The **Sensor-Service** automatically authenticates with:

* **Username:** `greenhouse_user`
* **Password:** `123456`

### 2\. User Login (Postman Testing)

Obtain a JWT token to access the domain services:

* **Endpoint:** `POST http://localhost:8080/api/auth/login`
* **Header:** `Content-Type: application/json`

### 3\. Accessing Services

Include the token in the **Authorization** header:

* **Key:** `Authorization`
* **Value:** `Bearer <your_token>`

-----

## 📊 Monitoring & Documentation

* **Eureka Dashboard:** View active services at [http://localhost:8761](https://www.google.com/search?q=http://localhost:8761).
* **Health Checks:** Access `/actuator/health` on any service.
* **Postman Collection:** Found in the root directory as `AGMS_Collection.json`.
* **Architecture Proof:** Screenshot available at `docs/eureka-dashboard.png`.

-----