package com.ibra.projecttracker.entity;

import com.ibra.projecttracker.enums.TaskAssignmentStatus;
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

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "task_id", referencedColumnName = "taskId")
        @ToString.Exclude
        private Task task;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "developer_id")
        @ToString.Exclude
        private Developer developer;

        private LocalDateTime assignedOn;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User assignedBy;

        private TaskAssignmentStatus status;

        private LocalDateTime deadline;

        private LocalDateTime completedOn;

        @Column(name = "last_notified_at")
        private LocalDateTime lastNotifiedAt;

}

