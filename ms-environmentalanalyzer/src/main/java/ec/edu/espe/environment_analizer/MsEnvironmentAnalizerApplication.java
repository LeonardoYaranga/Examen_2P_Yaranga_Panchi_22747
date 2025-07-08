package ec.edu.espe.environment_analizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan("ec.edu.espe.environment_analizer.entity")
@EnableJpaRepositories("ec.edu.espe.environment_analizer.repository")
public class MsEnvironmentAnalizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsEnvironmentAnalizerApplication.class, args);
    }

}
