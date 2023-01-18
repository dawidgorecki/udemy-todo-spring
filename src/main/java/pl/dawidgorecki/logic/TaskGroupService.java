package pl.dawidgorecki.logic;

import pl.dawidgorecki.model.TaskGroup;
import pl.dawidgorecki.model.projection.GroupReadModel;
import pl.dawidgorecki.model.projection.GroupWriteModel;
import pl.dawidgorecki.repository.TaskGroupRepository;
import pl.dawidgorecki.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    public TaskGroupService(TaskGroupRepository repository, TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        TaskGroup group = repository.save(source.toGroup());
        return new GroupReadModel(group);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks.");
        }

        TaskGroup taskGroup = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group with given id not found"));

        taskGroup.setDone(!taskGroup.isDone());
        repository.save(taskGroup);
    }
}
