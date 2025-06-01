package com.ibra.projecttracker.entity;

import com.ibra.projecttracker.enums.TaskStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name="task_assignments")
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
}

