package pl.dawidgorecki.model.projection;

import pl.dawidgorecki.model.Project;
import pl.dawidgorecki.model.TaskGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupWriteModel {
    @NotBlank(message = "Task group's description must not be empty")
    private String description;
    @Valid
    private List<GroupTaskWriteModel> tasks = new ArrayList<>();

    public GroupWriteModel() {
        tasks.add(new GroupTaskWriteModel());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GroupTaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(List<GroupTaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroup toGroup(Project project) {
        TaskGroup taskGroup = new TaskGroup();
        taskGroup.setDescription(description);
        taskGroup.setTasks(
                tasks.stream()
                        .map(source -> source.toTask(taskGroup))
                        .collect(Collectors.toList())
        );

        taskGroup.setProject(project);
        return taskGroup;
    }
}
