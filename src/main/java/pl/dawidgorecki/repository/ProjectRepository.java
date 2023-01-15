package pl.dawidgorecki.repository;

import pl.dawidgorecki.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    List<Project> findAll();

    Optional<Project> findById(Integer id);

    Project save(Project entity);
}
