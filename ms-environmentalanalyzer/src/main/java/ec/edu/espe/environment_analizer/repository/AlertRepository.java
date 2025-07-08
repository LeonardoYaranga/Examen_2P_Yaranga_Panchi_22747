package ec.edu.espe.environment_analizer.repository;

import ec.edu.espe.environment_analizer.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findBySensorId(String sensorId);
    List<Alert> findByType(String type);
}
