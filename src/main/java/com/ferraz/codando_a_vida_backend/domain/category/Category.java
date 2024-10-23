package com.ferraz.codando_a_vida_backend.domain.category;

import com.ferraz.codando_a_vida_backend.domain.auditable.AuditableEntity;
import com.ferraz.codando_a_vida_backend.domain.auditable.NewAuditableDTO;
import com.ferraz.codando_a_vida_backend.domain.auditable.UpdateAuditableDTO;
import com.ferraz.codando_a_vida_backend.domain.category.dto.NewCategoryDTO;
import com.ferraz.codando_a_vida_backend.domain.category.dto.UpdateCategoryDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_CATEGORIES")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Category extends AuditableEntity {

    @Column(name = "NAME")
    private String name;

    @Override
    public <T extends NewAuditableDTO> void create(T dto) {
        NewCategoryDTO newCategoryDTO = (NewCategoryDTO) dto;
        this.name = newCategoryDTO.name();
    }

    @Override
    public <T extends UpdateAuditableDTO> void update(T dto) {
        UpdateCategoryDTO updateCategoryDTO = (UpdateCategoryDTO) dto;
        this.name = updateCategoryDTO.name();
    }

}
