package com.ibra.projecttracker.dto;

import com.ibra.projecttracker.enums.ProjectStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectListDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;

    @Size(min = 3, max = 100, message = "Project name must be between 3 and 100 characters")
    private String name;

    private ProjectStatus status;
}
