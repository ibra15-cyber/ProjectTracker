package com.ibra.projecttracker.specification;


import com.ibra.projecttracker.entity.Project;
import com.ibra.projecttracker.enums.ProjectStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ProjectSpecification {
    public static Specification<Project> hasProjectName(String projectName) {
        return (root, query, criteriaBuilder) ->
                projectName != null ? criteriaBuilder.like(root.get("name"), "%" + projectName + "%") : null;
    }
    public static Specification<Project> hasDescription(String description) {
        return (root, query, criteriaBuilder) ->
                description != null ?  criteriaBuilder.like(root.get("description"), "%" + description + "%"): null;
    }
    public static Specification<Project> hasStatus(ProjectStatus status) {
        return (root, query, criteriaBuilder) ->
                status != null ? criteriaBuilder.equal(root.get("status"), status) : null;
    }
    public static Specification<Project> hasProjectId(Long projectId) {
        return (root, query, criteriaBuilder) ->
                projectId != null ? criteriaBuilder.equal(root.get("projectId"), projectId) : null;
    }

    public static Specification<Project> createdBetween(LocalDateTime from, LocalDateTime to) {
        return ((root, query, criteriaBuilder) -> {
            if(from != null && to != null) {
                return criteriaBuilder.between(root.get("createdAt"), from, to);
            } else if (from != null) {
                return criteriaBuilder.greaterThan(root.get("createdAt"), from);
            } else if (to != null) {
                return criteriaBuilder.lessThan(root.get("createdAt"), to);
            } else {
                return null;
            }
        });
    }

    public static Specification<Project> hasDeadline(LocalDateTime deadline) {
        return (root, query, criteriaBuilder) ->
                deadline != null ? criteriaBuilder.equal(root.get("deadline"), deadline): null;
    }

    public static Specification<Project> dueOnOrAfter(LocalDateTime dateTime) {
        return (root, query, criteriaBuilder) ->
                dateTime != null ? criteriaBuilder.greaterThanOrEqualTo(root.get("deadline"), dateTime) : null;
    }

    public static Specification<Project> dueOnOrBefore(LocalDateTime dateTime) {
        return (root, query, criteriaBuilder) ->
                dateTime != null ? criteriaBuilder.lessThanOrEqualTo(root.get("deadline"), dateTime) : null;
    }

}
