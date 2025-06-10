package com.ibra.projecttracker.entity;

import jakarta.persistence.*;

@Entity
@Table(name="admins")
public class Admin {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long adminId;

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private User user;
}
