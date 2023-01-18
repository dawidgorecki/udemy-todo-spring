package pl.dawidgorecki.logic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.dawidgorecki.model.TaskGroup;
import pl.dawidgorecki.repository.TaskGroupRepository;
import pl.dawidgorecki.repository.TaskRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {
    @Test
    @DisplayName("Should throw IllegalStateException if undone tasks exists")
    void toggleGroup_hasUndoneTasks_throwsIllegalStateException() {
        // given
        TaskRepository mockTaskRepository = taskRepositoryReturning(true);
        var toTest = new TaskGroupService(null, mockTaskRepository);

        // when
        var exception = Assertions.catchThrowable(() -> toTest.toggleGroup(0));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("has undone tasks");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when all tasks done and no group for given id")
    void toggleGroup_allTasksDone_noGroup_throwsIllegalArgumentException() {
        // given
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.findById(anyInt())).thenReturn(Optional.empty());

        var toTest = new TaskGroupService(mockGroupRepository, mockTaskRepository);

        // when
        var exception = Assertions.catchThrowable(() -> toTest.toggleGroup(0));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given id not found");
    }

    @Test
    void toggleGroup() {
        // given
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        // and
        var group = new TaskGroup();
        boolean beforeToggle = group.isDone();
        // and
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));

        var toTest = new TaskGroupService(mockGroupRepository, mockTaskRepository);

        // when
        toTest.toggleGroup(1);

        // then
        assertThat(group.isDone()).isEqualTo(!beforeToggle);
    }

    private TaskRepository taskRepositoryReturning(boolean t) {
        var taskRepository = mock(TaskRepository.class);
        when(taskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(t);
        return taskRepository;
    }
}