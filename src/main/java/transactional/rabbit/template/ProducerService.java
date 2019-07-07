package transactional.rabbit.template;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class ProducerService {

    private final TransactionalRabbitTemplate rabbitTemplate;

    @Transactional
    public void normalExecution() {
        rabbitTemplate.convertAndSend("normalExecution");
    }

    @Transactional
    public void executionWithException() {
        rabbitTemplate.convertAndSend("executionWithException");
        throw new RuntimeException();
    }
}
