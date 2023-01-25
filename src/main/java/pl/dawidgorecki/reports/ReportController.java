package pl.dawidgorecki.reports;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dawidgorecki.model.Task;
import pl.dawidgorecki.repository.TaskRepository;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final TaskRepository taskRepository;
    private final PersistedTaskEventRepository eventRepository;

    public ReportController(TaskRepository taskRepository, PersistedTaskEventRepository eventRepository) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/count/{id}")
    public ResponseEntity<TaskWithCount> readTaskWithCount(@PathVariable int id) {
        return taskRepository.findById(id)
                .map(task -> new TaskWithCount(task, eventRepository.findByTaskId(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private static class TaskWithCount {
        private String description;
        private boolean done;
        private int count;

        public TaskWithCount(Task task, List<PersistedTaskEvent> events) {
            description = task.getDescription();
            done = task.isDone();
            count = events.size();
        }

        public String getDescription() {
            return description;
        }

        public boolean isDone() {
            return done;
        }

        public int getCount() {
            return count;
        }
    }
}
