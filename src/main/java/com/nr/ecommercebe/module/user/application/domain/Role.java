package com.nr.ecommercebe.module.user.application.domain;

import com.nr.ecommercebe.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity(name = "roles")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends BaseEntity {
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    public Role(String id, RoleName name) {
        this.id = id;
        this.name = name;
    }
}

