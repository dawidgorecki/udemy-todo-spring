package pl.dawidgorecki.logic;

import pl.dawidgorecki.TaskConfigurationProperties;
import pl.dawidgorecki.model.Project;
import pl.dawidgorecki.model.Task;
import pl.dawidgorecki.model.TaskGroup;
import pl.dawidgorecki.model.projection.GroupReadModel;
import pl.dawidgorecki.model.projection.GroupTaskWriteModel;
import pl.dawidgorecki.model.projection.GroupWriteModel;
import pl.dawidgorecki.model.projection.ProjectWriteModel;
import pl.dawidgorecki.repository.ProjectRepository;
import pl.dawidgorecki.repository.TaskGroupRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private ProjectRepository projectRepository;
    private TaskGroupRepository groupRepository;
    private TaskConfigurationProperties config;
    private TaskGroupService service;

    public ProjectService(ProjectRepository projectRepository, TaskGroupRepository groupRepository, TaskConfigurationProperties config, TaskGroupService service) {
        this.projectRepository = projectRepository;
        this.groupRepository = groupRepository;
        this.config = config;
        this.service = service;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project create(ProjectWriteModel toSave) {
        return projectRepository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if (!config.getTemplate().isAllowMultipleTasks() && groupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }

        return projectRepository.findById(projectId)
                .map(project -> {
                    GroupWriteModel targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(s -> {
                                        var task = new GroupTaskWriteModel();
                                        task.setDescription(s.getDescription());
                                        task.setDeadline(deadline.plusDays(s.getDaysToDeadline()));
                                        return task;
                                    }).collect(Collectors.toList())

                    );
                    return service.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}
