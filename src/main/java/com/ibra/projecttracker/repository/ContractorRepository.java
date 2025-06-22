package com.ibra.projecttracker.repository;

import com.ibra.projecttracker.entity.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractorRepository extends JpaRepository<Contractor, Long> {
}
