package ec.edu.espe.notification_dispatcher.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.notification_dispatcher.dto.AlertDto;
import ec.edu.espe.notification_dispatcher.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private static final Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "notification.cola")
    public void receiveAlert(String json) {
        try {
            AlertDto dto = objectMapper.readValue(json, AlertDto.class);
            logger.info("Received alert: {}", dto);
            notificationService.processEvent(dto);
        } catch (Exception e) {
            logger.error("Error processing alert: {}", json, e);
        }
    }
}