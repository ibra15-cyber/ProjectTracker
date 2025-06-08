package com.ibra.projecttracker.utility.event;

import com.ibra.projecttracker.entity.TaskAssignment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TaskDueEvent extends ApplicationEvent {
    private final TaskAssignment taskAssignment;
    private final String developerEmail;

    public TaskDueEvent(Object source, TaskAssignment taskAssignment, String developerEmail) {
        super(source);
        this.taskAssignment = taskAssignment;
        this.developerEmail = developerEmail;
    }
}
