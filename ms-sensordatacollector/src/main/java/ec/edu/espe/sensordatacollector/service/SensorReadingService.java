package ec.edu.espe.sensordatacollector.service;


import com.fasterxml.jackson.databind.ObjectMapper;

import ec.edu.espe.sensordatacollector.dto.EventDto;
import ec.edu.espe.sensordatacollector.dto.SensorReadingDto;
import ec.edu.espe.sensordatacollector.model.SensorReading;
import ec.edu.espe.sensordatacollector.repository.SensorReadingRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SensorReadingService {
    private static final Logger logger = LoggerFactory.getLogger(SensorReadingService.class);

    private final SensorReadingRepository sensorReadingRepository;
    private final AnalyzerProducer analyzerProducer;

    public SensorReading saveSensorReading(SensorReadingDto dto) {
        // Validaciones
        if (dto.getSensorId() == null || dto.getSensorId().isEmpty()) {
            logger.error("Invalid sensorId: {}", dto);
            throw new IllegalArgumentException("sensorId cannot be null or empty");
        }
        if (dto.getType() == null || dto.getType().isEmpty()) {
            logger.error("Invalid type: {}", dto);
            throw new IllegalArgumentException("type cannot be null or empty");
        }
        if (dto.getValue() == null) {
            logger.error("Invalid value: {}", dto);
            throw new IllegalArgumentException("value cannot be null");
        }
        if (dto.getTimestamp() == null) {
            logger.error("Invalid timestamp: {}", dto);
            throw new IllegalArgumentException("timestamp cannot be null");
        }
        if ("temperature".equalsIgnoreCase(dto.getType()) && dto.getValue() > 60.0) {
            logger.error("Temperature out of range (> 60°C): {}", dto);
            throw new IllegalArgumentException("Temperature value exceeds 60°C");
        }

        try {
            SensorReading reading = new SensorReading();
            reading.setSensorId(dto.getSensorId());
            reading.setType(dto.getType());
            reading.setValue(dto.getValue());
            reading.setTimestamp(dto.getTimestamp());

            SensorReading savedReading = sensorReadingRepository.save(reading);
            logger.info("Sensor reading saved: {}", savedReading);

            // Enviar evento a analyzer.cola
            analyzerProducer.sendEvent(
                    savedReading.getId(),
                    dto.getSensorId(),
                    dto.getType(),
                    dto.getValue(),
                    dto.getTimestamp()
            );

            return savedReading;
        } catch (Exception e) {
            logger.error("Error saving sensor reading: {}", dto, e);
            throw new RuntimeException("Failed to save sensor reading", e);
        }
    }

    public List<SensorReading> getReadingsBySensorId(String sensorId) {
        try {
            if (sensorId == null || sensorId.isEmpty()) {
                logger.error("Invalid sensorId: {}", sensorId);
                throw new IllegalArgumentException("sensorId cannot be null or empty");
            }
            return sensorReadingRepository.findBySensorId(sensorId);
        } catch (Exception e) {
            logger.error("Error fetching readings for sensorId: {}", sensorId, e);
            throw new RuntimeException("Failed to fetch readings", e);
        }
    }
}