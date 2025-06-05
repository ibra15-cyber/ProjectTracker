package com.ibra.projecttracker.repository;

import com.ibra.projecttracker.entity.Project;
import com.ibra.projecttracker.entity.Task;
import com.ibra.projecttracker.enums.TaskStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> , JpaSpecificationExecutor<Task>  {
    List<Task> findByProject(Project project);

    @Query("SELECT t FROM Task t " +
            "WHERE t.deadline < CURRENT_TIMESTAMP AND" +
            " t.status != com.ibra.projecttracker.enums.TaskStatus.DONE")
    List<Task> findOverdueTasks();

    @Query("SELECT p.name, t.status, COUNT(t)" +
            " FROM Project p JOIN p.tasks t" +
            " GROUP BY p.name, t.status")
    List<Object[]> findTaskCountsGroupedByStatusAndProject();

}
