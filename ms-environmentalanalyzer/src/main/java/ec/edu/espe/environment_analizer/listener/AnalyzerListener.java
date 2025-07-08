package ec.edu.espe.environment_analizer.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.environment_analizer.dto.EventDto;
import ec.edu.espe.environment_analizer.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyzerListener {
    private static final Logger logger = LoggerFactory.getLogger(AnalyzerListener.class);

    private final AlertService alertService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "analyzer.cola")
    public void receiveEvent(String messageJson) {
        try {
            EventDto eventDto = objectMapper.readValue(messageJson, EventDto.class);
            logger.info("Received event: {}", eventDto);
            alertService.analyzeSensorReading(eventDto);
        } catch (Exception e) {
            logger.error("Error processing event: {}", messageJson, e);
        }
    }
}