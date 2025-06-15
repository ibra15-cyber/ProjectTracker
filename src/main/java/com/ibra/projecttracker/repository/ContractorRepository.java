package com.ibra.projecttracker.repository;

import com.ibra.projecttracker.entity.Contractor;
import com.ibra.projecttracker.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractorRepository extends JpaRepository<Contractor, Long> {
}
