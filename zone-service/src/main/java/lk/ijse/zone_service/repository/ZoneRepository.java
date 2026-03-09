package lk.ijse.zone_service.repository;

import lk.ijse.zone_service.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
}