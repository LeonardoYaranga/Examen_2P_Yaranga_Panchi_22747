package ec.edu.espe.sensordatacollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("ec.edu.espe.sensordatacollector.model")
public class MsSensorDataCollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsSensorDataCollectorApplication.class, args);
	}

}
