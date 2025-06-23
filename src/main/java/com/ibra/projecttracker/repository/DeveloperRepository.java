package com.ibra.projecttracker.repository;


import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.enums.DevSkills;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Optional<Developer> findByEmail(String email);

    @Query("SELECT d " +
            "FROM Developer d JOIN d.taskAssignments ta " +
            "GROUP BY d " +
            "ORDER BY COUNT(ta) " +
            "DESC " +
            "LIMIT 5")
    List<Developer> findTop5DevelopersWithMostTasksAssigned();


    @Query("SELECT d " +
            "FROM Developer d LEFT JOIN d.taskAssignments ta " +
            "GROUP BY d.id " +
            "ORDER BY COUNT(ta) " +
            "DESC")
    Page<Developer> findTop5ByTaskCount(Pageable pageable);

    List<Developer> findBySkill(DevSkills skill);

}
