package com.ibra.projecttracker.entity;

import com.ibra.projecttracker.enums.UserRole;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "managers")
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("MANAGER")

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Manager extends User {
    private String department;

    public Manager(String firstName, String lastName,String password, String email, String phoneNumber,
                 String department) {
        super(firstName, lastName, password, email, phoneNumber,  UserRole.ADMIN);
        this.department = department;
    }
}