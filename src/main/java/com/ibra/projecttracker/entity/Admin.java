package com.ibra.projecttracker.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="admins")
public class Admin {
    @Id
    private Long adminId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User user;
}
