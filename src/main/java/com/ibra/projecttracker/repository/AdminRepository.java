package com.ibra.projecttracker.repository;

import com.ibra.projecttracker.entity.Admin;
import com.ibra.projecttracker.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findByAdminLevel(String adminLevel);
}

