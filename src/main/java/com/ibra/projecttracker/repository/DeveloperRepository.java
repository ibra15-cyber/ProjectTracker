package com.ibra.projecttracker.repository;

import com.ibra.projecttracker.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
}
