package com.ibra.projecttracker.repository;

import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.entity.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Long> {
    List<TaskAssignment> findByDeveloper(Developer developer);
}

