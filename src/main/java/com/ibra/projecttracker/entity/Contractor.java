package com.ibra.projecttracker.entity;

import jakarta.persistence.*;

@Entity
@Table(name="contractors")
public class Contractor {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long contractorId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
