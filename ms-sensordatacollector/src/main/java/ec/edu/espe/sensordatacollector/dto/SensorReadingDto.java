package ec.edu.espe.sensordatacollector.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class SensorReadingDto {
    private String sensorId;
    private String type;
    private Double value;
    private Instant timestamp;
}