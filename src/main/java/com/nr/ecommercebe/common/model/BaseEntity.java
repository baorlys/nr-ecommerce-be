package com.nr.ecommercebe.common.model;

import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Objects;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
@FilterDef(name = "softDeleteFilter", parameters = @ParamDef(name = "isDeleted", type = boolean.class)) // Define filter for soft delete
@Filter(name = "softDeleteFilter", condition = "is_deleted = :isDeleted") // Filter condition for soft delete
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseEntity {
    @Id
    @Column(length = 26, nullable = false, updatable = false) // ULID is 26 characters long
    protected String id;

    @CreatedDate
    @Column(updatable = false)
    LocalDate createdOn;

    @LastModifiedDate
    LocalDate updateOn;

    @Column(columnDefinition = "boolean default false")
    boolean isDeleted;

    @PrePersist
    protected void initUlid() {
        if (this.id == null) {
            this.id = UlidCreator.getUlid().toString();
        }
    }


    @PreRemove
    public void onPreRemove() {
        this.isDeleted = true; // Perform soft delete by setting isDeleted to true
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
