package pl.dawidgorecki.model.event;

import pl.dawidgorecki.model.Task;

import java.time.Clock;

public class TaskDoneEvent extends TaskEvent {
    public TaskDoneEvent(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
