package com.ibra.projecttracker.entity;

import com.ibra.projecttracker.enums.TaskAssignmentStatus;
import com.ibra.projecttracker.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="task_assignments")
public class TaskAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskAssignmentId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    @ToString.Exclude
    private Task task;

    @ManyToOne
    @JoinColumn(name = "developer_id")
    @ToString.Exclude
    private Developer developer;

    private LocalDateTime assignedOn;

    private TaskAssignmentStatus status;

    private LocalDateTime dueOn;

    private LocalDateTime completedOn;

}

