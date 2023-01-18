package pl.dawidgorecki.logic;

import pl.dawidgorecki.TaskConfigurationProperties;
import pl.dawidgorecki.model.Project;
import pl.dawidgorecki.model.Task;
import pl.dawidgorecki.model.TaskGroup;
import pl.dawidgorecki.model.projection.GroupReadModel;
import pl.dawidgorecki.repository.ProjectRepository;
import pl.dawidgorecki.repository.TaskGroupRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private ProjectRepository projectRepository;
    private TaskGroupRepository groupRepository;
    private TaskConfigurationProperties config;

    public ProjectService(ProjectRepository projectRepository, TaskGroupRepository groupRepository, TaskConfigurationProperties config) {
        this.projectRepository = projectRepository;
        this.groupRepository = groupRepository;
        this.config = config;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project create(Project entity) {
        return projectRepository.save(entity);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if (!config.getTemplate().isAllowMultipleTasks() && groupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }

        TaskGroup result = projectRepository.findById(projectId)
                .map(project -> {
                    TaskGroup taskGroup = new TaskGroup();
                    taskGroup.setDescription(project.getDescription());
                    taskGroup.setProject(project);
                    taskGroup.setTasks(
                            project.getSteps().stream()
                                    .map(s -> new Task(s.getDescription(), deadline.plusDays(s.getDaysToDeadline())))
                                    .collect(Collectors.toList())
                    );

                    return groupRepository.save(taskGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));

        return new GroupReadModel(result);
    }
}
