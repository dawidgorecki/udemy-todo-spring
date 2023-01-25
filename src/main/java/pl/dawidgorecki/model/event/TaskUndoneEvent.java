package pl.dawidgorecki.model.event;

import pl.dawidgorecki.model.Task;

import java.time.Clock;

public class TaskUndoneEvent extends TaskEvent {
    public TaskUndoneEvent(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
