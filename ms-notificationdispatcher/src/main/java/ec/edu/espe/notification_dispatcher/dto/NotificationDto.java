package ec.edu.espe.notification_dispatcher.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NotificationDto {
    private String notificationId;
    private String eventType;
    private String recipient;
    private String status;
    private LocalDateTime timestamp;
    private String priority;
}
