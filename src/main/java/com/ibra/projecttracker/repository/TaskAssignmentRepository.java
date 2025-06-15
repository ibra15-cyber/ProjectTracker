package com.ibra.projecttracker.repository;

import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.entity.Task;
import com.ibra.projecttracker.entity.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Long> {
    List<TaskAssignment> findByDeveloper(Developer developer);

    @Query(value = """
        SELECT ta.* 
        FROM task_assignments ta
        INNER JOIN tasks t ON ta.task_id = t.task_id
        WHERE t.deadline > (NOW() AT TIME ZONE 'UTC')
        AND t.status != 3
        ORDER BY t.deadline ASC
        """, nativeQuery = true)
    List<TaskAssignment> findTaskAssignmentsWithFutureDueDates();


    @Query(value = "SELECT EXISTS (" +
            "SELECT 1 FROM task_assignments ta " +
            "JOIN tasks t ON ta.task_id = t.task_id " +
            "JOIN developers d ON ta.developer_id = d.developer_id " +
            "JOIN users u ON d.user_id = u.user_id " +
            "WHERE t.task_id = :taskId AND u.user_id = :userId" +
            ")", nativeQuery = true)
    boolean existsByTaskAndDeveloperUserNative(@Param("taskId") Long taskId, @Param("userId") Long userId);




}

