package ec.edu.espe.environment_analizer.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public static final String QUEUE = "analyzer.cola";

    @Bean
    public Queue analyzerQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }
}