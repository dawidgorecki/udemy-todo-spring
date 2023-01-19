package pl.dawidgorecki.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dawidgorecki.logic.TaskGroupService;
import pl.dawidgorecki.model.Task;
import pl.dawidgorecki.model.projection.GroupReadModel;
import pl.dawidgorecki.model.projection.GroupWriteModel;
import pl.dawidgorecki.repository.TaskRepository;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class TaskGroupController {
    private final TaskGroupService service;
    private final TaskRepository repository;

    public TaskGroupController(TaskGroupService service, TaskRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<GroupReadModel>> readAllGroups() {
        List<GroupReadModel> body = service.readAll();
        return ResponseEntity.ok(body);
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<Void> toggleGroup(@PathVariable int id) {
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<GroupReadModel> createGroup(@RequestBody GroupWriteModel toCreate) {
        GroupReadModel group = service.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/tasks/" + group.getId())).body(group);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(repository.readAllByGroup_Id(id));
    }
}
