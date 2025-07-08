package ec.edu.espe.environment_analizer.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.environment_analizer.dto.EventDto;
import ec.edu.espe.environment_analizer.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import com.rabbitmq.client.Channel;

@Component
@RequiredArgsConstructor
public class AnalyzerListener {
    private static final Logger logger = LoggerFactory.getLogger(AnalyzerListener.class);

    private final AlertService alertService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "analyzer.cola")
    public void receiveEvent(String messageJson, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            logger.debug("Received raw message from analyzer.cola: {}", messageJson);
            EventDto eventDto = objectMapper.readValue(messageJson, EventDto.class);
            logger.info("Deserialized event: {}", eventDto);
            alertService.analyzeSensorReading(eventDto);
            // Confirmar mensaje manualmente
            channel.basicAck(deliveryTag, false);
            logger.debug("Acknowledged message with deliveryTag: {}", deliveryTag);
        } catch (Exception e) {
            logger.error("Error processing message from analyzer.cola: {}", messageJson, e);
            try {
                // Rechazar mensaje y enviarlo a la cola de errores (si está configurada)
                channel.basicNack(deliveryTag, false, false);
                logger.debug("Rejected message with deliveryTag: {}", deliveryTag);
            } catch (IOException ex) {
                logger.error("Error rejecting message with deliveryTag: {}", deliveryTag, ex);
            }
        }
    }
}