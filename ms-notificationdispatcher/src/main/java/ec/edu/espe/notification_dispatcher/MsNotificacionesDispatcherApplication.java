package ec.edu.espe.notification_dispatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan("ec.edu.espe.notification_dispatcher.entity")
@EnableJpaRepositories("ec.edu.espe.notification_dispatcher.repository")
public class MsNotificacionesDispatcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsNotificacionesDispatcherApplication.class, args);
    }

}
