package com.jworks.afro.pixels.service.entities;

import com.jworks.afro.pixels.service.enums.Level;
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
@Table(name = "verification_level")
public class VerificationLevel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "level", nullable = false,length = 20, unique = true)
    @Enumerated(EnumType.STRING)
    private Level level;
}
