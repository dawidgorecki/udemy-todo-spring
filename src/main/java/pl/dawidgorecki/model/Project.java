package pl.dawidgorecki.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank(message = "Project description must not be empty")
    private String description;

    @OneToMany(mappedBy = "project")
    private List<TaskGroup> groups;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectStep> steps;

    public Integer getId() {
        return id;
    }

    void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    List<TaskGroup> getGroups() {
        return groups;
    }

    void setGroups(List<TaskGroup> groups) {
        this.groups = groups;
    }

    public List<ProjectStep> getSteps() {
        return steps;
    }

    void setSteps(List<ProjectStep> steps) {
        this.steps = steps;
    }
}
