package pl.dawidgorecki.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.dawidgorecki.logic.ProjectService;
import pl.dawidgorecki.model.Project;
import pl.dawidgorecki.model.ProjectStep;
import pl.dawidgorecki.model.projection.ProjectWriteModel;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    public String showProjects(Model model) {
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

    @PostMapping
    public String addProject(
            @Valid @ModelAttribute("project") ProjectWriteModel current,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "projects";
        }

        service.create(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("projects", getProjects());
        model.addAttribute("message", "Dodano projekt");
        return "projects";
    }

    @PostMapping(params = "addStep")
    public String addProjectStep(@ModelAttribute("project") ProjectWriteModel project) {
        project.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping("/{id}")
    public String createGroup(
            @PathVariable int id,
            @ModelAttribute("project") ProjectWriteModel current,
            Model model,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline
    ) {
        try {
            service.createGroup(id, deadline);
            model.addAttribute("message", "Dodano grupę");
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("message", "Błąd podczas tworzenia grupy");
        }

        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects() {
        return service.readAll();
    }
}
