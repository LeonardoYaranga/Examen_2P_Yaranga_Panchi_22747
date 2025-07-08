package ec.edu.espe.notification_dispatcher.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import ec.edu.espe.notification_dispatcher.dto.AlertDto;
import ec.edu.espe.notification_dispatcher.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private static final Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "notification.cola")
    public void receiveAlert(String json, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            logger.debug("Received raw message from notification.cola: {}", json);
            AlertDto dto = objectMapper.readValue(json, AlertDto.class);
            logger.info("Received alert: {}", dto);
            notificationService.processEvent(dto);
            // Confirmar mensaje manualmente
            channel.basicAck(deliveryTag, false);
            logger.debug("Acknowledged message with deliveryTag: {}", deliveryTag);
        } catch (Exception e) {
            logger.error("Error processing alert: {}", json, e);
            try {
                // Rechazar mensaje sin reencolar (puede ir a una cola de errores si está configurada)
                channel.basicNack(deliveryTag, false, false);
                logger.debug("Rejected message with deliveryTag: {}", deliveryTag);
            } catch (IOException ex) {
                logger.error("Error rejecting message with deliveryTag: {}", deliveryTag, ex);
            }
        }
    }
}