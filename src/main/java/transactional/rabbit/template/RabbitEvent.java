package transactional.rabbit.template;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class RabbitEvent {
    @NonNull
    private final Runnable taskToSend;

    public static RabbitEvent rabbitEvent(Runnable taskToSend) {
        return builder().taskToSend(taskToSend).build();
    }
}
