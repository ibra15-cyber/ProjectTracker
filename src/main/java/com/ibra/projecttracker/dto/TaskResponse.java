package com.ibra.projecttracker.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@Data
public class TaskResponse {
    private String message;
    private String statusCode;
    private Long timestamp;

    private TaskDTO task;
    private List<TaskDTO> tasks;
}
