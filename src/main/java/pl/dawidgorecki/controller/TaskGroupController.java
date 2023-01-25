package pl.dawidgorecki.controller;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.dawidgorecki.logic.TaskGroupService;
import pl.dawidgorecki.model.Task;
import pl.dawidgorecki.model.projection.GroupReadModel;
import pl.dawidgorecki.model.projection.GroupTaskWriteModel;
import pl.dawidgorecki.model.projection.GroupWriteModel;
import pl.dawidgorecki.model.projection.ProjectWriteModel;
import pl.dawidgorecki.repository.TaskRepository;

import javax.print.attribute.standard.Media;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class TaskGroupController {
    private final TaskGroupService service;
    private final TaskRepository repository;

    public TaskGroupController(TaskGroupService service, TaskRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<GroupReadModel>> readAllGroups() {
        List<GroupReadModel> body = service.readAll();
        return ResponseEntity.ok(body);
    }

    @Transactional
    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> toggleGroup(@PathVariable int id) {
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<GroupReadModel> createGroup(@RequestBody GroupWriteModel toCreate) {
        GroupReadModel group = service.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/tasks/" + group.getId())).body(group);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(repository.readAllByGroup_Id(id));
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String showGroups(Model model) {
        model.addAttribute("group", new GroupWriteModel());
        return "groups";
    }

    @PostMapping(params = "addTask", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addTask(@ModelAttribute("group") GroupWriteModel current) {
        current.getTasks().add(new GroupTaskWriteModel());
        return "groups";
    }
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addGroup(
            @Valid @ModelAttribute("group") GroupWriteModel current,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "groups";
        }

        service.createGroup(current);
        model.addAttribute("group", new GroupWriteModel());
        model.addAttribute("groups", getGroups());
        model.addAttribute("message", "Dodano grupę");
        return "groups";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ModelAttribute("groups")
    public List<GroupReadModel> getGroups() {
        return service.readAll();
    }
}
