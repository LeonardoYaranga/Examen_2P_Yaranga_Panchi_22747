package ec.edu.espe.notification_dispatcher.repository;

import ec.edu.espe.notification_dispatcher.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}