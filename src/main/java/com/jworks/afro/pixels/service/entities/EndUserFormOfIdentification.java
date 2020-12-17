package com.jworks.afro.pixels.service.entities;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
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
@Table(name = "end_user_form_of_identification",
        indexes = @Index(name = "IS_DOCUMENT_APPROVED_INDEX", columnList = "is_approved"))
public class EndUserFormOfIdentification extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "end_user_id", referencedColumnName = "id")
    private EndUser endUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "form_of_identification_id",referencedColumnName = "id")
    private FormOfIdentification formOfIdentification;

    @Column(nullable = false)
    private String documentImageUrl;

    @Column(name = "is_approved", length = 50, nullable = false)
    @ColumnDefault("0")
    private boolean isApproved;

}
