package ec.edu.espe.environment_analizer.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AlertDTO {
    private String alertId;
    private String type;
    private String sensorId;
    private double value;
    private double threshold;
    private LocalDateTime timestamp;
}
