package ec.edu.espe.environment_analizer.service;

import ec.edu.espe.environment_analizer.dto.AlertDTO;
import ec.edu.espe.environment_analizer.dto.EventDto;
import ec.edu.espe.environment_analizer.entity.Alert;
import ec.edu.espe.environment_analizer.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;



@Service
@RequiredArgsConstructor
public class AlertService {
    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);

    private final AlertRepository alertRepository;
    private final NotificationProducer notificationProducer;

    public void analyzeSensorReading(EventDto eventDto) {
        try {
            logger.info("Received event: {}", eventDto);
            AlertDTO alertDto = null;
            switch (eventDto.getType().toLowerCase()) {
                case "temperature":
                    if (eventDto.getValue() > 40.0) {
                        alertDto = createAlert(eventDto, "HighTemperatureAlert", 40.0);
                    } else {
                        logger.debug("Temperature value {} does not exceed threshold 40.0", eventDto.getValue());
                    }
                    break;
                case "humidity":
                    if (eventDto.getValue() < 20.0) {
                        alertDto = createAlert(eventDto, "LowHumidityWarning", 20.0);
                    } else {
                        logger.debug("Humidity value {} does not fall below threshold 20.0", eventDto.getValue());
                    }
                    break;
                case "seismic":
                    if (eventDto.getValue() > 3.0) {
                        alertDto = createAlert(eventDto, "SeismicActivityDetected", 3.0);
                    } else {
                        logger.debug("Seismic value {} does not exceed threshold 3.0", eventDto.getValue());
                    }
                    break;
                default:
                    logger.debug("No alert generated for type: {}", eventDto.getType());
            }
            logger.info("Alert: {}", alertDto);
            if (alertDto != null) {
                // Guardar alerta
                Alert alert = new Alert();
                alert.setAlertId(alertDto.getAlertId());
                alert.setType(alertDto.getType());
                alert.setSensorId(alertDto.getSensorId());
                alert.setValue(alertDto.getValue());
                alert.setThreshold(alertDto.getThreshold());
                alert.setTimestamp(alertDto.getTimestamp());
                alertRepository.save(alert);

                // Publicar evento de alerta
                notificationProducer.sendAlert(alertDto);

                logger.info("Alert generated and sent to notification.cola: {}", alertDto);
            } else {
                logger.info("No alert generated for event: {}", eventDto);
            }
        } catch (Exception e) {
            logger.error("Error analyzing sensor reading: {}", eventDto, e);
            throw new RuntimeException("Failed to analyze sensor reading", e);
        }
    }

    public AlertDTO createAlert(EventDto eventDto, String type, double threshold) {
        AlertDTO alertDto = new AlertDTO();
        alertDto.setAlertId("ALT-" + eventDto.getEventId().replace("EVT-", ""));
        alertDto.setType(type);
        alertDto.setSensorId(eventDto.getSensorId());
        alertDto.setValue(eventDto.getValue());
        alertDto.setThreshold(threshold);
        alertDto.setTimestamp(LocalDateTime.now());
        return alertDto;
    }
}


