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
@Table(name = "end_user_image",
        indexes = {
        @Index(name = "IS_IMG_ACTIVE_INDEX", columnList = "is_active"),
        @Index(name = "NAME_INDEX", columnList = "name")
})
public class EndUserImage extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "end_user_image_category_id", referencedColumnName = "id")
    private EndUserImageCategory endUserImageCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private EndUser endUser;

    @Column(length = 150)
    private String tag;

    @Column(nullable = false)
    private String description;

    @Column(length = 200, nullable = false)
    private String imageUrl;

    @Column(name = "is_active",nullable = false)
    @ColumnDefault("0")
    private boolean isActive;
}
