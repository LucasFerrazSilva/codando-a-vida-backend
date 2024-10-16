package com.ferraz.codando_a_vida_backend.domain.user;

import com.ferraz.codando_a_vida_backend.infra.security.dto.RegisterDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private UserStatus status;

    @Column(name = "CREATE_DATE_TIME")
    private LocalDateTime createDate;

    @Column(name = "UPDATE_DATE_TIME")
    private LocalDateTime updateDate;


    public User() {
        this.status = UserStatus.ACTIVE;
        this.createDate = LocalDateTime.now();
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
        return UserStatus.ACTIVE.equals(this.status);
    }
}
