package pl.dawidgorecki.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

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
    private Set<ProjectStep> steps;

    public Integer getId() {
        return id;
    }

    void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    List<TaskGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<TaskGroup> groups) {
        this.groups = groups;
    }

    public Set<ProjectStep> getSteps() {
        return steps;
    }

    public void setSteps(Set<ProjectStep> steps) {
        this.steps = steps;
    }
}
