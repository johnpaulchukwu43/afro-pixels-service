package com.jworks.afro.pixels.service.entities;

import com.jworks.afro.pixels.service.enums.Role;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
@Table(name = "end_user_role")
public class EndUserRole extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "end_user_id", referencedColumnName = "id")
    private EndUser endUser;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

}
