package pl.dawidgorecki.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.dawidgorecki.model.event.TaskDoneEvent;
import pl.dawidgorecki.model.event.TaskEvent;
import pl.dawidgorecki.model.event.TaskUndoneEvent;

@Component
public class TaskEventListener {
    private static final Logger logger = LoggerFactory.getLogger(TaskEventListener.class);
    private final PersistedTaskEventRepository eventRepository;

    public TaskEventListener(PersistedTaskEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @EventListener
    @Async
    public void on(TaskDoneEvent event) {
        onChanged(event);
    }

    @EventListener
    @Async
    public void on(TaskUndoneEvent event) {
        onChanged(event);
    }

    private void onChanged(TaskEvent event) {
        logger.info("Got {}", event);
        eventRepository.save(new PersistedTaskEvent(event));
    }
}
