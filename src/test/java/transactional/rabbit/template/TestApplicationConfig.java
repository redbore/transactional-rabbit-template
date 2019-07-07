package transactional.rabbit.template;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestApplicationConfig {

    @Bean
    public RabbitTemplate testRabbitTemplate(ConnectionFactory connectionFactory, Queue transactionalRabbitTemplateQueue) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setDefaultReceiveQueue(transactionalRabbitTemplateQueue.getName());
        return rabbitTemplate;
    }
}
