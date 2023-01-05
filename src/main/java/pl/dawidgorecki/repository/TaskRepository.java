package pl.dawidgorecki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import pl.dawidgorecki.model.Task;

@RepositoryRestResource
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
