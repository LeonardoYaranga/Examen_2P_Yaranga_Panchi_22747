package ec.edu.espe.sensordatacollector.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
//    public static final String ANALYZER_QUEUE = "analyzer.cola";
//
//    @Bean
//    public Queue notificationQueue() {
//        return new Queue(ANALYZER_QUEUE, true);
//    }
}