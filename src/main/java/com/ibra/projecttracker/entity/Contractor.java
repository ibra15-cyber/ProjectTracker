package com.ibra.projecttracker.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="contractors")
public class Contractor {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long contractorId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}
