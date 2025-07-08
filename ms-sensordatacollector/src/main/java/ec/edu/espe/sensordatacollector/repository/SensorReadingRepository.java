package ec.edu.espe.sensordatacollector.repository;


import ec.edu.espe.sensordatacollector.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
    List<SensorReading> findBySensorId(String sensorId);

}
