package com.ibra.projecttracker.entity;

import com.ibra.projecttracker.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.sql.ast.tree.update.Assignment;

import java.time.LocalDateTime;
import java.util.Set;

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
    private LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "projectId")
    private Project project;

    @OneToMany(mappedBy = "task")
    private Set<TaskAssignment> taskAssignments;

}
