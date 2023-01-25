package pl.dawidgorecki.model.event;

import pl.dawidgorecki.model.Task;

import java.time.Clock;
import java.time.Instant;

public abstract class TaskEvent {
    private int taskId;
    private Instant occurrence;

    public static TaskEvent changed(Task source) {
        return source.isDone() ? new TaskDoneEvent(source) : new TaskUndoneEvent(source);
    }

    public TaskEvent(int taskId, Clock clock) {
        this.taskId = taskId;
        occurrence = Instant.now(clock);
    }

    public int getTaskId() {
        return taskId;
    }

    public Instant getOccurrence() {
        return occurrence;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                "taskId=" + taskId +
                ", occurrence=" + occurrence +
                '}';
    }
}
