package transactional.rabbit.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public TopicExchange transactionalRabbitTemplateExchange(@Value("${transactional.rabbit.template.exchange}") String exchangeName) {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue transactionalRabbitTemplateQueue(@Value("${transactional.rabbit.template.queue}") String queueName) {
        return new Queue(queueName);
    }

    @Bean
    public String transactionalRabbitTemplateRoutingKey(@Value("${transactional.rabbit.template.routing.key}") String routingKey) {
        return routingKey;
    }

    @Bean
    public Binding transactionalRabbitTemplateBinding(
        Queue transactionalRabbitTemplateQueue,
        TopicExchange transactionalRabbitTemplateExchange,
        String transactionalRabbitTemplateRoutingKey
    ) {
        return BindingBuilder
            .bind(transactionalRabbitTemplateQueue)
            .to(transactionalRabbitTemplateExchange)
            .with(transactionalRabbitTemplateRoutingKey);
    }

    @Bean
    public TransactionalRabbitTemplate transactionalRabbitTemplate(
        ApplicationEventPublisher eventPublisher,
        ConnectionFactory connectionFactory,
        MessageConverter messageConverter,
        TopicExchange transactionalRabbitTemplateExchange,
        String transactionalRabbitTemplateRoutingKey
    ) {
        TransactionalRabbitTemplate rabbitTemplate = new TransactionalRabbitTemplate(connectionFactory, eventPublisher);
        rabbitTemplate.setExchange(transactionalRabbitTemplateExchange.getName());
        rabbitTemplate.setRoutingKey(transactionalRabbitTemplateRoutingKey);
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTransactionManager rabbitTransactionManager(ConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }

    @Bean
    public ProducerService producerService(TransactionalRabbitTemplate transactionalRabbitTemplate) {
        return new ProducerService(transactionalRabbitTemplate);
    }

    @Bean
    public TransactionalRabbitEventListener transactionalRabbitEventListener() {
        return new TransactionalRabbitEventListener();
    }
}
