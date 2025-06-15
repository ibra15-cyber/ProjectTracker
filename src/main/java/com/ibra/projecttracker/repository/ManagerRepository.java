package com.ibra.projecttracker.repository;

import com.ibra.projecttracker.entity.Manager;
import com.ibra.projecttracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
