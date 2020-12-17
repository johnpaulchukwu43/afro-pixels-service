package com.jworks.afro.pixels.service.entities;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "form_of_identification",  indexes = @Index(name = "IS_FORM_OF_IDENTIFICATION_ACTIVE_IDX", columnList = "is_active"))
public class FormOfIdentification extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "is_active",nullable = false)
    @ColumnDefault("1")
    private boolean isActive;
}
