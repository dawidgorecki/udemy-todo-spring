package pl.dawidgorecki.model.projection;

import org.junit.jupiter.api.Test;
import pl.dawidgorecki.model.Task;
import pl.dawidgorecki.model.TaskGroup;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GroupReadModelTest {
    @Test
    void testConstructor() {
        // given
        TaskGroup taskGroup = new TaskGroup();
        taskGroup.setDescription("Test");
        taskGroup.setTasks(List.of(new Task("foo", null)));

        // when
        GroupReadModel groupReadModel = new GroupReadModel(taskGroup);

        // then
        assertThat(groupReadModel).hasFieldOrPropertyWithValue("deadline", null);
    }
}