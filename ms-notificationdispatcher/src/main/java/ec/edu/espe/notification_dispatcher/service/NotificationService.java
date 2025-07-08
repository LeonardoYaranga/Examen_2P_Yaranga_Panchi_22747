package ec.edu.espe.notification_dispatcher.service;

import ec.edu.espe.notification_dispatcher.dto.AlertDto;
import ec.edu.espe.notification_dispatcher.dto.NotificationDto;
import ec.edu.espe.notification_dispatcher.entity.Notification;
import ec.edu.espe.notification_dispatcher.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@EnableScheduling
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final RestTemplate restTemplate;

    private final List<NotificationDto> lowPriorityNotifications = new ArrayList<>();

    public void processEvent(AlertDto alert) {
        String priority = determinePriority(alert.getType());
        NotificationDto notification = new NotificationDto();
        notification.setNotificationId(UUID.randomUUID().toString());
        notification.setEventType(alert.getType());
        notification.setRecipient("user@example.com"); // Ejemplo, puede configurarse
        notification.setStatus("PENDING");
        notification.setTimestamp(LocalDateTime.now());
        notification.setPriority(priority);

        if ("CRITICAL".equals(priority)) {
            sendNotification(notification);
        } else {
            synchronized (lowPriorityNotifications) {
                lowPriorityNotifications.add(notification);
                logger.info("Added low-priority notification: {}", notification);
            }
        }
    }

    private String determinePriority(String eventType) {
        switch (eventType) {
            case "HighTemperatureAlert":
            case "SeismicActivityDetected":
                return "CRITICAL";
            case "DailyReportGenerated":
                return "INFO";
            case "SensorInactiveAlert":
                return "WARNING";
            default:
                return "INFO";
        }
    }

    private void sendNotification(NotificationDto notification) {
        try {
            // Simular envío de notificaciones
            if (notification.getRecipient().contains("example.com")) {
                // Simular correo
                restTemplate.postForObject("http://localhost:8000/api/conjunta/2p/notifications/mock-email", notification, String.class);
                logger.info("Correo enviado: {} a {}", notification.getEventType(), notification.getRecipient());
            } else {
                // Simular SMS
                restTemplate.postForObject("http://localhost:/api/conjunta/2p/notifications/mock-sms", notification, String.class);
                logger.info("SMS enviado: {} a {}", notification.getEventType(), notification.getRecipient());
            }
            // Simular push notification
            logger.info("Push notification sent: {} a {}", notification.getEventType(), notification.getRecipient());
            notification.setStatus("SENT");
        } catch (Exception e) {
            logger.error("Error enviando notificación: {}", notification, e);
            notification.setStatus("FAILED");
        }

        // Guardar notificación en CockroachDB
        Notification entity = new Notification();
        entity.setNotificationId(notification.getNotificationId());
        entity.setEventType(notification.getEventType());
        entity.setRecipient(notification.getRecipient());
        entity.setStatus(notification.getStatus());
        entity.setTimestamp(notification.getTimestamp());
        entity.setPriority(notification.getPriority());
        notificationRepository.save(entity);
    }

    @Scheduled(cron = "0 0/30 * * * ?") // Cada 30 minutos
    public void sendLowPriorityNotifications() {
        List<NotificationDto> notificationsToSend;
        synchronized (lowPriorityNotifications) {
            notificationsToSend = new ArrayList<>(lowPriorityNotifications);
            lowPriorityNotifications.clear();
        }
        for (NotificationDto notification : notificationsToSend) {
            sendNotification(notification);
        }
        logger.info("Sent {} low-priority notifications", notificationsToSend.size());
    }
}
