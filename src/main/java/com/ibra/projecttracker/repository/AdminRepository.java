package com.ibra.projecttracker.repository;

import com.ibra.projecttracker.entity.Admin;
import com.ibra.projecttracker.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
