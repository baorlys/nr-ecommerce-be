package com.nr.ecommercebe.common.model;

import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Objects;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseEntity {
    @Id
    @Column(length = 26, nullable = false, updatable = false) // ULID is 26 characters long
    protected String id;

    @CreatedDate
    @Column(updatable = false)
    LocalDate createdOn;

    @LastModifiedDate
    LocalDate updatedOn;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    @PrePersist
    protected void initUlid() {
        if (this.id == null) {
            this.id = UlidCreator.getUlid().toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return id != null && id.equals(that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
