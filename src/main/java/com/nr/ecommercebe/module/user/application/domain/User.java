package com.nr.ecommercebe.module.user.application.domain;

import com.nr.ecommercebe.shared.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {
    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @Email
    @Column(unique = true, nullable = false)
    String email;

    @Column(unique = true)
    String phone;

    @ToString.Exclude
    @Column(nullable = false)
    String passwordHash;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    Role role;

    public User(String id) {
        this.id = id;
    }
}
