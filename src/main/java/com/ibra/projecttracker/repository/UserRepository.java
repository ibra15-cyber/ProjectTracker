package com.ibra.projecttracker.repository;


import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.entity.Manager;
import com.ibra.projecttracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE TYPE(u) = Developer")
    List<Developer> findAllDevelopers();

    @Query("SELECT u FROM User u WHERE TYPE(u) = Manager")
    List<Manager> findAllManagers();

    boolean existsByEmail(String email);
}