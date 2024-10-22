package com.ferraz.codando_a_vida_backend.domain.auditable;

import com.ferraz.codando_a_vida_backend.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private EntityStatus status;

    @Column(name = "CREATE_DATE_TIME")
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATE_USER")
    private User createUser;

    @Column(name = "UPDATE_DATE_TIME")
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATE_USER")
    private User updateUser;


    protected AuditableEntity() {
        this(null);
    }

    protected AuditableEntity(User createUser) {
        this.status = EntityStatus.ACTIVE;
        this.createDate = LocalDateTime.now();
        this.createUser = createUser;
    }

    public abstract <T extends NewAuditableDTO> void create(T dto);

    public abstract <T extends UpdateAuditableDTO> void update(T dto, User updateUser);


    public <T extends NewAuditableDTO> void create(T dto, User createUser) {
        create(dto);
        this.status = EntityStatus.ACTIVE;
        this.createDate = LocalDateTime.now();
        this.createUser = createUser;
    }

    public void update(User updateUser) {
        this.updateDate = LocalDateTime.now();
        this.updateUser = updateUser;
    }

    public void inactivate(User updateUser) {
        this.status = EntityStatus.INACTIVE;
        this.updateUser = updateUser;
        this.updateDate = LocalDateTime.now();
    }

}
