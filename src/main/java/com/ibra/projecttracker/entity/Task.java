package com.ibra.projecttracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibra.projecttracker.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name="tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "projectId")
    private Project project;

    @JsonIgnore
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskAssignment> taskAssignments;

}
