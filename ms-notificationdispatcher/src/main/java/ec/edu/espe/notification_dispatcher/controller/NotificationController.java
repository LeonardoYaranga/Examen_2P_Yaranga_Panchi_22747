package ec.edu.espe.notification_dispatcher.controller;

import ec.edu.espe.notification_dispatcher.dto.NotificationDto;
import ec.edu.espe.notification_dispatcher.entity.Notification;
import ec.edu.espe.notification_dispatcher.repository.NotificationRepository;
import ec.edu.espe.notification_dispatcher.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        if (notifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<NotificationDto> notificationDTOs = notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(notificationDTOs);
    }

    private NotificationDto convertToDTO(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setNotificationId(notification.getNotificationId());
        dto.setEventType(notification.getEventType());
        dto.setRecipient(notification.getRecipient());
        dto.setStatus(notification.getStatus());
        dto.setTimestamp(notification.getTimestamp());
        dto.setPriority(notification.getPriority());
        return dto;
    }
}