
package ec.edu.espe.sensordatacollector.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "sensor_readings")
@Data
public class SensorReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sensorId;

    @Column(nullable = false)
    private String type; // TEMPERATURE, HUMIDITY, POLLUTANT, SEISMIC

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private Instant timestamp;
}
