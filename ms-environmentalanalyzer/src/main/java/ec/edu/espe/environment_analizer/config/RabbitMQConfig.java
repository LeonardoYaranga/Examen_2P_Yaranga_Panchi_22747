package ec.edu.espe.environment_analizer.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableRabbit
public class RabbitMQConfig {
    public static final String ANALYZER_QUEUE = "analyzer.cola";
//    public static final String NOTIFICATION_QUEUE = "notification.cola";

    @Bean
    public Queue analyzerQueue() {
        return new Queue(ANALYZER_QUEUE, true);
    }

//    @Bean
//    public Queue notificationQueue() {
//        return new Queue(NOTIFICATION_QUEUE, true);
//    }
}