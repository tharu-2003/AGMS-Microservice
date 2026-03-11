package lk.ijse.automation_service.repository;

import lk.ijse.automation_service.entity.AutomationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutomationLogRepository extends JpaRepository<AutomationLog, Long> {
}