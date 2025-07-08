package ec.edu.espe.environment_analizer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.environment_analizer.dto.AlertDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {
    private static final Logger logger = LoggerFactory.getLogger(NotificationProducer.class);
    private static final String QUEUE = "notification.cola";

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void sendAlert(AlertDTO alertDto) {
        try {
            String json = objectMapper.writeValueAsString(alertDto);
            rabbitTemplate.convertAndSend(QUEUE, json);
            logger.info("Alert sent to notification.cola: {}", alertDto);
        } catch (Exception e) {
            logger.error("Error sending alert: {}", alertDto, e);
        }
    }
}