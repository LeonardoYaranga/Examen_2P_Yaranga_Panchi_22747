package ec.edu.espe.notification_dispatcher.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String notificationId;
    private String eventType;
    private String recipient;
    private String status;
    private LocalDateTime timestamp;
    private String priority;
}
