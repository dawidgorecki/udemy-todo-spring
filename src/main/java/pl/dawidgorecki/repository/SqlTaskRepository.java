package pl.dawidgorecki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dawidgorecki.model.Task;

@Repository
public interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {
}
