package transactional.rabbit.template;

import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.synchronizedList;

public class TransactionalRabbitEventListener {

    private final List<RabbitEvent> unsentRabbitEvents = synchronizedList(new ArrayList<>());

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void sendMessagesBeforeCommit(RabbitEvent rabbitEvent) {
        try {
            rabbitEvent
                .getTaskToSend()
                .run();
        } catch (Exception e) {
            e.printStackTrace();
            unsentRabbitEvents.add(rabbitEvent);
        }
    }
}
