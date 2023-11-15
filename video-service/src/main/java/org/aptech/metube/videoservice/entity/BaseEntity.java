package org.aptech.metube.videoservice.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class BaseEntity {
//    @Column(name = "created_by")
//    private String createdBy;
//
//    @Column(name = "created_date")
//    private LocalDateTime createdDate;
//
//    @Column(name = "modified_by")
//    private String modifiedBy;
//
//    @Column(name = "modified_date")
//    private LocalDateTime modifiedDate;
//
//    @Column(name = "is_deleted")
//    private Boolean isDeleted = false;
//
//
//    @PrePersist
//    private void prePersist() {
//        setCreatedDate(LocalDateTime.now());
//    }
//
//    @PreUpdate
//    private void preUpdate() {
//        setModifiedDate(LocalDateTime.now());
//    }
}
