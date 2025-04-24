package com.nr.ecommercebe.module.user.model;

import com.nr.ecommercebe.common.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
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
}
