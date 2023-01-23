package pl.dawidgorecki.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.dawidgorecki.TaskConfigurationProperties;
import pl.dawidgorecki.repository.ProjectRepository;
import pl.dawidgorecki.repository.TaskGroupRepository;
import pl.dawidgorecki.repository.TaskRepository;

@Configuration
public class LogicConfiguration {
    @Bean
    public ProjectService projectService(
            ProjectRepository projectRepository,
            TaskGroupRepository groupRepository,
            TaskGroupService taskGroupService,
            TaskConfigurationProperties config
    ) {
        return new ProjectService(projectRepository, groupRepository, config, taskGroupService);
    }

    @Bean
    public TaskGroupService taskGroupService(
            TaskGroupRepository groupRepository,
            TaskRepository taskRepository
    ) {
        return new TaskGroupService(groupRepository, taskRepository);
    }
}
