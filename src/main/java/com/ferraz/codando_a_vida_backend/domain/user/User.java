package com.ferraz.codando_a_vida_backend.domain.user;

import com.ferraz.codando_a_vida_backend.domain.AuditableEntity;
import com.ferraz.codando_a_vida_backend.domain.EntityStatus;
import com.ferraz.codando_a_vida_backend.infra.security.dto.RegisterDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "TB_USERS")
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends AuditableEntity implements UserDetails {

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PASSWORD")
    private String password;


    public User() {
        super();
    }

    public User(RegisterDTO registerDTO, PasswordEncoder passwordEncoder) {
        this();

        this.name = registerDTO.name();
        this.email = registerDTO.email();
        this.password = passwordEncoder.encode(registerDTO.password());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return EntityStatus.ACTIVE.equals(this.getStatus());
    }
}
