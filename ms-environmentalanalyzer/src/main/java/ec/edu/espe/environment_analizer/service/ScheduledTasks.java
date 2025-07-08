package ec.edu.espe.environment_analizer.service;

import ec.edu.espe.environment_analizer.dto.AlertDTO;
import ec.edu.espe.environment_analizer.entity.Alert;
import ec.edu.espe.environment_analizer.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final AlertRepository alertRepository;
    private final NotificationProducer notificationProducer;

    // Generación de reportes diarios (cada 24 horas a medianoche)
    @Scheduled(cron = "0 0 0 * * ?") // Medianoche todos los días
    public void generateDailyReport() {
        try {
            // Simular generación de reporte (p.ej., promedio de lecturas)
            AlertDTO report = new AlertDTO();
            report.setAlertId("ALT-" + UUID.randomUUID().toString());
            report.setType("DailyReportGenerated");
            report.setValue(0.0); // Simulado, podría calcularse desde sensor_readings
            report.setThreshold(0.0); // No aplicable para reportes
            report.setTimestamp(LocalDateTime.now());

            // Guardar en CockroachDB
            Alert alertEntity = new Alert();
            alertEntity.setAlertId(report.getAlertId());
            alertEntity.setType(report.getType());
            alertEntity.setValue(report.getValue());
            alertEntity.setThreshold(report.getThreshold());
            alertEntity.setTimestamp(report.getTimestamp());
            alertRepository.save(alertEntity);

            // Publicar evento a notification.cola
            notificationProducer.sendAlert(report);
            logger.info("Daily report generated and sent: {}", report);
        } catch (Exception e) {
            logger.error("Error generating daily report", e);
        }
    }

    // Verificación de sensores inactivos (cada 6 horas)
    @Scheduled(cron = "0 0 */6 * * ?") // Cada 6 horas (00:00, 06:00, 12:00, 18:00)
    public void checkInactiveSensors() {
        try {
            // Simular detección de sensor inactivo (sin lecturas en 24 horas)
            AlertDTO alert = new AlertDTO();
            alert.setAlertId("ALT-" + UUID.randomUUID().toString());
            alert.setType("SensorInactiveAlert");
            alert.setSensorId("SENSOR_123"); // Ejemplo, debería obtenerse de una consulta
            alert.setValue(0.0); // No aplicable
            alert.setThreshold(0.0); // No aplicable
            alert.setTimestamp(LocalDateTime.now());

            // Guardar en CockroachDB
            Alert alertEntity = new Alert();
            alertEntity.setAlertId(alert.getAlertId());
            alertEntity.setType(alert.getType());
            alertEntity.setSensorId(alert.getSensorId());
            alertEntity.setValue(alert.getValue());
            alertEntity.setThreshold(alert.getThreshold());
            alertEntity.setTimestamp(alert.getTimestamp());
            alertRepository.save(alertEntity);

            // Publicar evento a notification.cola
            notificationProducer.sendAlert(alert);
            logger.info("Inactive sensor alert sent: {}", alert);
        } catch (Exception e) {
            logger.error("Error checking inactive sensors", e);
        }
    }

    // Archivado de datos antiguos (cada 7 días, los domingos a medianoche)
    @Scheduled(cron = "0 0 0 * * SUN") // Medianoche los domingos
    public void archiveOldData() {
        try {
            // Simular archivado de datos > 6 meses
            logger.info("Archiving data older than 6 months...");
            // En una implementación real, esto sería una consulta SQL para mover datos
            // Ejemplo: DELETE FROM sensor_readings WHERE timestamp < NOW() - INTERVAL '6 months'
        } catch (Exception e) {
            logger.error("Error archiving old data", e);
        }
    }
}
