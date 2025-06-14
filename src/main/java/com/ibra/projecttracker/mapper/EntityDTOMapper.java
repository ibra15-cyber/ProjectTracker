package com.ibra.projecttracker.mapper;

import com.ibra.projecttracker.dto.*;
import com.ibra.projecttracker.entity.*;
import org.springframework.stereotype.Component;

@Component
public class EntityDTOMapper {

    public ProjectDTO mapProjectToProjectDTO(Project project) {
        if (project == null) {
            return null;
        }
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getProjectId());
        projectDTO.setName(project.getName());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setStatus(project.getStatus());
        projectDTO.setCreatedAt(project.getCreatedAt());
        projectDTO.setDeadline(project.getDeadline());
        return projectDTO;
    }

    public TaskDTO mapTaskToTaskDTO(Task task) {
        if (task == null) {
            return null;
        }

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getTaskId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setCreatedAt(task.getCreatedAt());
        taskDTO.setDeadline(task.getDeadline());
        taskDTO.setProjectId(task.getProject().getProjectId());
        return taskDTO;
    }

    public DeveloperDTO mapDeveloperToDeveloperDTO(Developer developer) {
        if (developer == null) {
            return null;
        }
        DeveloperDTO developerDTO = new DeveloperDTO();
        developerDTO.setId(developer.getDeveloperId());
        developerDTO.setName(developer.getName());
        developerDTO.setEmail(developer.getEmail());
        developerDTO.setSkill(developer.getSkills());
        return developerDTO;
    }

    public UserDTO mapUserToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getUserRole()
        );
    }


    public TaskAssignmentDTO mapTaskAssignmentToDTO(TaskAssignment taskAssignment) {
        if (taskAssignment == null) {
            return null;
        }

        TaskAssignmentDTO dto = new TaskAssignmentDTO();
        dto.setTaskAssignmentId(taskAssignment.getTaskAssignmentId());
        dto.setTaskId(taskAssignment.getTask().getTaskId());
        dto.setTaskTitle(taskAssignment.getTask().getTitle());
        dto.setDeveloperId(taskAssignment.getDeveloper().getDeveloperId());
        dto.setDeveloperName(taskAssignment.getDeveloper().getName());
        dto.setAssignedOn(taskAssignment.getAssignedOn());
        dto.setStatus(taskAssignment.getStatus());
        dto.setDeadline(taskAssignment.getDeadline());
        dto.setCompletedOn(taskAssignment.getCompletedOn());
        return dto;
    }
}

