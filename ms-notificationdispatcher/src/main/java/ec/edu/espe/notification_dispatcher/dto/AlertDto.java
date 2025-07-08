package ec.edu.espe.notification_dispatcher.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AlertDto {
    private String alertId;
    private String type;
    private String sensorId;
    private double value;
    private double threshold;
    private LocalDateTime timestamp;
}
