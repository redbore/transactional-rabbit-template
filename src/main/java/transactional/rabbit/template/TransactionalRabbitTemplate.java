package transactional.rabbit.template;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;

public class TransactionalRabbitTemplate extends RabbitTemplate {
    private final ApplicationEventPublisher eventPublisher;

    public TransactionalRabbitTemplate(ConnectionFactory connectionFactory, ApplicationEventPublisher eventPublisher) {
        super(connectionFactory);
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void convertAndSend(Object message) {
        eventPublisher.publishEvent(RabbitEvent.rabbitEvent(() -> super.convertAndSend(message)));
    }
}
