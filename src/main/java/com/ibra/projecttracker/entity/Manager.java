package com.ibra.projecttracker.entity;

import jakarta.persistence.*;

@Entity
@Table(name="managers")
public class Manager {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long mangerId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
