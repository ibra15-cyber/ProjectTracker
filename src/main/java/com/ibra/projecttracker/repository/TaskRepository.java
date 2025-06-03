package com.ibra.projecttracker.repository;

import com.ibra.projecttracker.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
