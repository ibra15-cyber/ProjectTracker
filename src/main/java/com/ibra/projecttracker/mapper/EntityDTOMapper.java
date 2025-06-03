package com.ibra.projecttracker.mapper;

import com.ibra.projecttracker.dto.DeveloperDTO;
import com.ibra.projecttracker.dto.ProjectDTO;
import com.ibra.projecttracker.dto.TaskDTO;
import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.entity.Project;
import com.ibra.projecttracker.entity.Task;
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
        projectDTO.setDeadline(project.getDeadline());

//        List<TaskDTO> taskDTOs = project.getTasks().stream()
//                        .map(this::mapTaskToTaskDTO)
//                .collect(Collectors.toList());
        //is there a point were we pass list of taskdtos to taskdtos not sure
//        projectDTO.setTaskDTOS(taskDTOs);
        return projectDTO;
    }

//    public Project mapProjectDTOToProject(ProjectDTO projectDTO) {
//        Project project = new Project();
//        project.setProjectId(projectDTO.getId());
//        project.setName(projectDTO.getName());
//        project.setDescription(projectDTO.getDescription());
//        project.setStatus(projectDTO.getStatus());
//        project.setDeadline(projectDTO.getDeadline());
//        return project;
//    }

    public TaskDTO mapTaskToTaskDTO(Task task) {
        if (task == null) {
            return null;
        }

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getTaskId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setDueDate(task.getDueDate());
        taskDTO.setProjectId(task.getProject().getProjectId());
        taskDTO.setTaskAssignments(task.getTaskAssignments());
        return taskDTO;
    }

//    public Task mapTaskDTOToTask(TaskDTO taskDTO) {
//        Task task = new Task();
//        task.setTitle(taskDTO.getTitle());
//        task.setDescription(taskDTO.getDescription());
//        task.setStatus(taskDTO.getStatus());
//        task.setDueDate(taskDTO.getDueDate());
//        //use taskDTO to pass project Id, use it to get the project
//        //we might do this in service
//        Long projectId = taskDTO.getProjectId();

    /// /        task.setProject(taskDTO.getProjectId());
//        return task;
//    }
    public DeveloperDTO mapDeveloperToDeveloperDTO(Developer developer) {
        if (developer == null) {
            return null;
        }
        DeveloperDTO developerDTO = new DeveloperDTO();
        developerDTO.setId(developer.getDeveloperId());
        developerDTO.setName(developer.getName());
        developerDTO.setEmail(developer.getEmail());
        developerDTO.setSkill(developer.getSkills());
        // to assing task, you will need a dev created adn task created then set the teask to the dev
        //most importantly for oneToMany, the mone side can't take several record bc of the many part
        //but the many part can set several records of the one part
        return developerDTO;
    }

//    public Developer mapDeveloperDTOToDeveloper(DeveloperDTO developerDTO) {
//        Developer developer = new Developer();
//        developer.setDeveloperId(developerDTO.getId());
//        developer.setName(developerDTO.getName());
//        developer.setEmail(developerDTO.getEmail());
//        developer.setSkills(developerDTO.getSkill());
//        return developer;
//    }
//
    }

