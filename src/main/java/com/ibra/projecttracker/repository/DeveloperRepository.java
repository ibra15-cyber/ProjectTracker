package com.ibra.projecttracker.repository;

import com.ibra.projecttracker.dto.DeveloperDTO;
import com.ibra.projecttracker.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    @Query("SELECT d " +
            "FROM Developer d JOIN d.taskAssignments ta " +
            "GROUP BY d " +
            "ORDER BY COUNT(ta) " +
            "DESC " +
            "LIMIT 5")
    List<Developer> findTop5DevelopersWithMostTasksAssigned();
}
