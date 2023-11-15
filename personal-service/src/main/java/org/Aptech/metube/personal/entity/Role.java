package org.aptech.metube.personal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.aptech.metube.personal.constant.EntityStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "role")
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Role{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name = "status")
    private EntityStatusCode statusCode;
    @Column(name = "description")
    private String description;
    @JsonIgnoreProperties("roles")
    @ManyToMany(mappedBy = "roles")
    List<User> userList;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;


    @PrePersist
    private void prePersist() {
        setCreatedDate(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        setCreatedBy(userDetails.getUsername());
    }

    @PreUpdate
    private void preUpdate() {
        setModifiedDate(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        setModifiedBy(userDetails.getUsername());
    }
}
