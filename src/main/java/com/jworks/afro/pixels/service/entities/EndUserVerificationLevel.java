package com.jworks.afro.pixels.service.entities;

import lombok.*;

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
@Table(name = "user_verification_level")
public class EndUserVerificationLevel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "end_user_id", referencedColumnName = "id")
    private EndUser endUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "verification_level_id",referencedColumnName = "id")
    private VerificationLevel verificationLevel;

}
