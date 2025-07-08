package ec.edu.espe.environment_analizer.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SensorReadingDTO {
    private String sensorId;
    private String type;
    private double value;
    private LocalDateTime timestamp;

}
