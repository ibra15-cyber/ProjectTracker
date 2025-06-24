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
        developerDTO.setId(developer.getId());
        developerDTO.setFirstName(developer.getFirstName());
        developerDTO.setLastName(developer.getLastName());
        developerDTO.setEmail(developer.getEmail());
        developerDTO.setPhoneNumber(developer.getPhoneNumber());
        developerDTO.setUserRole(developer.getUserRole());
        developerDTO.setSkill(developer.getSkill());
        return developerDTO;
    }

    public UserDTO mapUserToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getId(),
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
        dto.setDeveloperId(taskAssignment.getDeveloper().getId());
        dto.setDeveloperName(taskAssignment.getDeveloper().getFirstName() + " " + taskAssignment.getDeveloper().getLastName());
        dto.setAssignedBy(taskAssignment.getAssignedBy().getFirstName() + " " + taskAssignment.getAssignedBy().getLastName());
        dto.setAssignedOn(taskAssignment.getAssignedOn());
        dto.setStatus(taskAssignment.getStatus());
        dto.setDeadline(taskAssignment.getDeadline());
        dto.setCompletedOn(taskAssignment.getCompletedOn());
        return dto;
    }

    public AdminDTO mapAdminToAdminDTO(Admin admin) {
        if (admin == null) {
            return null;
        }
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(admin.getId());
        adminDTO.setName(admin.getFirstName() + " " + admin.getLastName());
        adminDTO.setEmail(admin.getEmail());
        adminDTO.setAdminLevel(admin.getAdminLevel());
        return adminDTO;
    }

    public ManagerDTO mapManagerToManagerDTO(Manager manager) {
        if (manager == null) {
            return null;
        }
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setId(manager.getId());
        managerDTO.setName(manager.getFirstName() + " " + manager.getLastName());
        managerDTO.setEmail(manager.getEmail());
        managerDTO.setDepartment(manager.getDepartment());
        return managerDTO;
    }

    public ContractorDTO mapContractorToContractorDTO(Contractor contractor) {
        if (contractor == null) {
            return null;
        }
        ContractorDTO contractorDTO = new ContractorDTO();
        contractorDTO.setId(contractor.getId());
        contractorDTO.setName(contractor.getFirstName() + " " + contractor.getLastName());
        contractorDTO.setEmail(contractor.getEmail());
        contractorDTO.setCompanyName(contractor.getCompanyName());
        contractorDTO.setCompanyAddress(contractor.getCompanyAddress());
        return contractorDTO;
    }
}

