package com.ibra.projecttracker.entity;

import com.ibra.projecttracker.enums.UserRole;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "contractors") // Table name for this entity
@PrimaryKeyJoinColumn(name = "user_id") // Links admin.user_id to users.id
@DiscriminatorValue("CONTRACTOR") // Discriminator value for this entity type

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Contractor extends User {
    private String companyName;
    private String companyAddress;

    public Contractor(String firstName, String lastName, String password, String email, String phoneNumber,
                      String companyName, String companyAddress){
        super(firstName, lastName, password, email, phoneNumber,  UserRole.ADMIN);
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }
}