package pl.dawidgorecki.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.dawidgorecki.TaskConfigurationProperties;
import pl.dawidgorecki.model.Project;
import pl.dawidgorecki.model.ProjectStep;
import pl.dawidgorecki.model.TaskGroup;
import pl.dawidgorecki.model.projection.GroupReadModel;
import pl.dawidgorecki.repository.ProjectRepository;
import pl.dawidgorecki.repository.TaskGroupRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {
    @Test
    @DisplayName("Should throw IllegalStateException when only one group is allowed and undone group exists")
    void createGroup_noMultipleGroups_undoneGroupExists_ThrowsIllegalStateException() {
        // given
        var mockConfig = configurationReturning(false);
        var mockGroupRepository = groupRepositoryReturning(true);

        var service = new ProjectService(null, mockGroupRepository, mockConfig, null);

        // when
        var exception = catchThrowable(() -> service.createGroup(0, LocalDateTime.now()));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone group");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when only one group is allowed, no undone groups and no projects for given id")
    void createGroup_noMultipleGroups_undoneGroupNotExists_noProjects_ThrowsIllegalArgumentException2() {
        // given
        var mockConfig = configurationReturning(false);
        var mockGroupRepository = groupRepositoryReturning(false);
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        var service = new ProjectService(mockRepository, mockGroupRepository, mockConfig, null);

        // when
        var exception = catchThrowable(() -> service.createGroup(0, LocalDateTime.now()));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when many groups are allowed and no projects for given id")
    void createGroup_noProjects_throwsIllegalArgumentException() {
        // given
        var mockConfig = configurationReturning(true);
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        var service = new ProjectService(mockRepository, null, mockConfig, null);

        // when
        var exception = catchThrowable(() -> service.createGroup(0, LocalDateTime.now()));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("Should create a new group from project")
    void createGroup() {
        // given
        var mockConfig = configurationReturning(true);
        var mockRepository = mock(ProjectRepository.class);
        var project = getProject("Holidays", Set.of(-1, -2));
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(project));

        var groupRepository = new InMemoryTaskGroupRepository();
        var taskGroupService = new TaskGroupService(groupRepository, null);
        var service = new ProjectService(mockRepository, groupRepository, mockConfig, taskGroupService);

        int countBefore = groupRepository.count();
        // when
        LocalDateTime today = LocalDate.now().atStartOfDay();
        GroupReadModel group = service.createGroup(1, today);

        // then
        assertThat(group.getDescription()).isEqualTo("Holidays");
        assertThat(group.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(group.getTasks()).allMatch(task -> task.getDescription().equals("Step"));
        assertThat(countBefore + 1).isEqualTo(groupRepository.count());
    }

    private static class InMemoryTaskGroupRepository implements TaskGroupRepository {
        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if (entity.getId() == null) {
                entity.setId(++index);
            }

            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(g -> !g.isDone())
                    .anyMatch(g -> g.getProject() != null && g.getProject().getId().equals(projectId));
        }

        @Override
        public boolean existsByDescription(String description) {
            return map.values().stream()
                    .anyMatch(group -> group.getDescription().equals(description));
        }
    }

    ;

    private TaskConfigurationProperties configurationReturning(boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);

        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);

        return mockConfig;
    }

    private TaskGroupRepository groupRepositoryReturning(boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);

        return mockGroupRepository;
    }

    private Project getProject(String description, Set<Integer> daysToDeadline) {
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(description);

        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("Step");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                }).collect(Collectors.toSet());

        when(result.getSteps()).thenReturn(steps);
        return result;
    }
}