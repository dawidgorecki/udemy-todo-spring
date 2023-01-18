package pl.dawidgorecki.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dawidgorecki.model.Task;
import pl.dawidgorecki.repository.TaskRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping(params = {"!page", "!size", "!sort"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Exposing all tasks!");
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable pageable) {
        logger.warn("Custom pager");
        Page<Task> page = taskRepository.findAll(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        taskRepository.findById(id).ifPresent(t -> t.updateFrom(toUpdate));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> getTask(@PathVariable int id) {
        Optional<Task> task = taskRepository.findById(id);
        return ResponseEntity.of(task);
    }

    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate) {
        Task task = taskRepository.save(toCreate);
        return ResponseEntity.created(URI.create("/tasks/" + task.getId())).body(task);
    }


    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<Void> toggleTask(@PathVariable int id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        taskRepository.findById(id).ifPresent(t -> t.setDone(!t.isDone()));

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) {
        return ResponseEntity.ok(taskRepository.findByDone(state));
    }
}
