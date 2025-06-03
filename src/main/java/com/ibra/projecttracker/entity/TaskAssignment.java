package com.ibra.projecttracker.entity;

import com.ibra.projecttracker.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;

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
    private Task task;

    @ManyToOne
    @JoinColumn(name = "developer_id")
    private Developer developer;

    private LocalDateTime assignedOn;

    private TaskStatus status;

    private LocalDateTime completedOn;

}

