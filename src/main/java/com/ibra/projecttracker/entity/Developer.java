package com.ibra.projecttracker.entity;

import com.ibra.projecttracker.enums.DevSkills;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@Entity
@Table(name="developers")
@ToString(exclude = {"taskAssignments"})
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
