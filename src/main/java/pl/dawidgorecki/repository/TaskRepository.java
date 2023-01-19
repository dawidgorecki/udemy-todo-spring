package pl.dawidgorecki.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import pl.dawidgorecki.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Page<Task> findAll(Pageable pageable);

    Optional<Task> findById(Integer id);

    Task save(Task entity);

    List<Task> findByDone(@Param("state") boolean done);

    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    List<Task> readAllByGroup_Id(Integer groupId);
}
