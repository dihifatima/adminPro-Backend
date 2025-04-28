package com.example.security.Authentification.role;

import com.example.security.Authentification.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Role {

    @Id @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedAt;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
