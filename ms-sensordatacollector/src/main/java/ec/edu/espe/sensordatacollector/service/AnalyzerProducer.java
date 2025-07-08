package ec.edu.espe.sensordatacollector.service;


import com.fasterxml.jackson.databind.ObjectMapper;

import ec.edu.espe.sensordatacollector.dto.EventDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalyzerProducer {
    private static final Logger logger = LoggerFactory.getLogger(AnalyzerProducer.class);
    private static final String QUEUE = "analyzer.cola";

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void sendEvent(Long readingId, String sensorId, String type, Double value, Instant timestamp) {
        try {
            EventDto eventDto = new EventDto();
            eventDto.setEventId("EVT-" + readingId.toString());
            eventDto.setSensorId(sensorId);
            eventDto.setType(type);
            eventDto.setValue(value);
            eventDto.setTimestamp(timestamp);

            String message = objectMapper.writeValueAsString(eventDto);
            rabbitTemplate.convertAndSend(QUEUE, message);
            logger.info("Event sent to analyzer.cola: {}", eventDto);
        } catch (Exception e) {
            logger.error("Error sending event: sensorId={}, type={}, value={}", sensorId, type, value, e);
        }
    }
}