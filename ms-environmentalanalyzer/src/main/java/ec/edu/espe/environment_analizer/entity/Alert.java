package ec.edu.espe.environment_analizer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "alerts")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String alertId;
    private String type;
    private String sensorId;
    private double value;
    private double threshold;
    private LocalDateTime timestamp;


}
