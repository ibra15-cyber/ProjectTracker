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
    private Long developerId;

    @Size(min = 3, max = 50)
    private String name;
    @Email
    private String email;

    private DevSkills skills;

    @OneToMany(mappedBy = "developer")
    private Set<TaskAssignment> taskAssignments;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

}
