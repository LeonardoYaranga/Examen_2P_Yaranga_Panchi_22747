package ec.edu.espe.notification_dispatcher.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    public static final String QUEUE_NAME = "notification.cola";

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }
}
