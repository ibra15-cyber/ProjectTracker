package com.ibra.projecttracker.entity;

import com.ibra.projecttracker.enums.DevSkills;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
    @Email
    @NotBlank
    private String email;
    private DevSkills skills;

    @OneToMany(mappedBy = "developer")
    private Set<TaskAssignment> taskAssignments;
}
