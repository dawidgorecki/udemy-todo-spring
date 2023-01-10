package pl.dawidgorecki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.dawidgorecki.model.Task;

@Repository
public interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {
    @Override
    @Query(nativeQuery = true, value = "SELECT COUNT(*) > 0 FROM tasks WHERE id=:taskId")
    boolean existsById(@Param("taskId") Integer id);
}
