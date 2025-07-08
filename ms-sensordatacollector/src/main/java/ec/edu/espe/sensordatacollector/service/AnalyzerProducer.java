package ec.edu.espe.sensordatacollector.service;


import com.fasterxml.jackson.databind.ObjectMapper;

import ec.edu.espe.sensordatacollector.dto.EventDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyzerProducer {
    private static final Logger logger = LoggerFactory.getLogger(AnalyzerProducer.class);
    private static final String QUEUE = "analyzer.cola";

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void sendEvent(EventDto dto) {
        try {
            String json = objectMapper.writeValueAsString(dto);
            rabbitTemplate.convertAndSend(QUEUE, json);
            logger.info("Event sent to analyzer.cola: {}", dto);
        } catch (Exception e) {
            logger.error("Error sending event to analyzer.cola: {}", dto, e);
            // No resiliencia: los eventos se pueden perder si RabbitMQ está caído
        }
    }
}