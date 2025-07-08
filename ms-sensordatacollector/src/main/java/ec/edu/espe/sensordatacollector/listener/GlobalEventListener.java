//package ec.edu.espe.sensordatacollector.listener;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import ec.edu.espe.sensordatacollector.dto.EventDto;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class GlobalEventListener {
//    private static final Logger logger = LoggerFactory.getLogger(GlobalEventListener.class);
//
//
//    private final ObjectMapper objectMapper;
//
//    @RabbitListener(queues = "sensor.data.collector.queue")
//    public void handleGlobalEvent(String message) {
//        try {
//            EventDto eventDto = objectMapper.readValue(message, EventDto.class);
//            logger.info("Received global event: {}", eventDto.getType());
//
//            // Guardar evento para auditoría
//
//
//            // Procesar eventos específicos si es necesario
//            switch (eventDto.getType()) {
//                case "HighTemperatureAlert":
//                case "SeismicActivityDetected":
//                case "SystemOnlineEvent":
//                case "SystemBackOnlineEvent":
//                    logger.info("Processing {}: {}", eventDto.getType());
//                    break;
//                default:
//                    logger.debug("Ignoring event: {}", eventDto.getType());
//            }
//        } catch (Exception e) {
//            logger.error("Error processing global event: {}", message, e);
//        }
//    }
//}
