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
            AlertDTO alertDto = null;
            switch (eventDto.getType().toUpperCase()) {
                case "temperature":
                    if (eventDto.getValue() > 40.0) {
                        alertDto = createAlert(eventDto, "HighTemperatureAlert", 40.0);
                    }
                    break;
                case "humidity":
                    if (eventDto.getValue() < 20.0) {
                        alertDto = createAlert(eventDto, "LowHumidityWarning", 20.0);
                    }
                    break;
                case "seismic":
                    if (eventDto.getValue() > 3.0) {
                        alertDto = createAlert(eventDto, "SeismicActivityDetected", 3.0);
                    }
                    break;
                default:
                    logger.debug("No alert generated for type: {}", eventDto.getType());
            }

            if (alertDto != null) {
                // Guardar alerta
                Alert alert = new Alert();
                alert.setAlertId(alertDto.getAlertId());
                alert.setType(alertDto.getType());
                alert.setSensorId(alertDto.getSensorId());
                alert.setValue(alertDto.getValueattendance());
                alert.setThreshold(alertDto.getThreshold());
                alert.setTimestamp(alertDto.getTimestamp());
                alertRepository.save(alert);

                // Publicar evento de alerta
                notificationProducer.sendAlert(alertDto);

                logger.info("Alert generated: {}", alertDto);
            }
        } catch (Exception e) {
            logger.error("Error analyzing sensor reading: {}", eventDto, e);
        }


    }

    public AlertDTO createAlert(EventDto eventDto, String type, double threshold) {
        AlertDTO alertDto = new AlertDTO();
        alertDto.setAlertId("ALT-" + UUID.randomUUID().toString());
        alertDto.setType(type);
        alertDto.setSensorId(eventDto.getSensorId());
        alertDto.setValueattendance(eventDto.getValue());
        alertDto.setThreshold(threshold);
        alertDto.setTimestamp(LocalDateTime.from(Instant.now()));
        return alertDto;
    }
}


