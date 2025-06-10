package com.ibra.projecttracker.entity;

import com.ibra.projecttracker.enums.UserRole;
import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToOne(optional = true, mappedBy = "user")
    private Admin admin;

    @OneToOne(optional = true, mappedBy = "user")
    private Developer developer;

    @OneToOne(optional = true, mappedBy = "user")
    private Contractor contractor;

    @OneToOne(optional = true, mappedBy = "user")
    private Manager manager;


}
