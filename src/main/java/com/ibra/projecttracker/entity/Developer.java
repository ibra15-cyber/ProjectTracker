package com.ibra.projecttracker.entity;

import com.ibra.projecttracker.enums.DevSkills;
import jakarta.persistence.*;

import java.util.Set;

@Entity(name="developers")
public class Developer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long developerId;
    private String name;
    private String email;
    private DevSkills skills;

    @OneToMany(mappedBy = "developer")
    private Set<TaskAssignment> taskAssignments;
}
