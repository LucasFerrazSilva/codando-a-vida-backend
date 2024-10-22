package com.ferraz.codando_a_vida_backend.domain.user;

import com.ferraz.codando_a_vida_backend.domain.auditable.AuditableEntity;
import com.ferraz.codando_a_vida_backend.domain.auditable.EntityStatus;
import com.ferraz.codando_a_vida_backend.domain.auditable.NewAuditableDTO;
import com.ferraz.codando_a_vida_backend.domain.auditable.UpdateAuditableDTO;
import com.ferraz.codando_a_vida_backend.infra.security.dto.RegisterDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "TB_USERS")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends AuditableEntity implements UserDetails {

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Override
    public <T extends NewAuditableDTO> void create(T dto) {
        RegisterDTO registerDTO = (RegisterDTO) dto;

        this.name = registerDTO.name();
        this.email = registerDTO.email();
        this.password = new BCryptPasswordEncoder().encode(registerDTO.password());
    }

    @Override
    public <T extends UpdateAuditableDTO> void update(T dto) {
        // Implementar atualizacao de usuario
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
