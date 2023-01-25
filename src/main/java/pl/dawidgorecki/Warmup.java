package pl.dawidgorecki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import pl.dawidgorecki.model.Task;
import pl.dawidgorecki.model.TaskGroup;
import pl.dawidgorecki.repository.TaskGroupRepository;

import java.util.List;

@Component
public class Warmup implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(Warmup.class);
    private final TaskGroupRepository groupRepository;

    public Warmup(TaskGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Application warmup after context refreshed");
        final String description = "ApplicationContextEvent";

        if (!groupRepository.existsByDescription(description)) {
            logger.info("No group found");
            TaskGroup taskGroup = new TaskGroup();
            taskGroup.setDescription(description);
            taskGroup.setTasks(List.of(
                    new Task("Task 1", null, taskGroup),
                    new Task("Task 2", null, taskGroup),
                    new Task("Task 3", null, taskGroup)
            ));

            groupRepository.save(taskGroup);
        }
    }
}
