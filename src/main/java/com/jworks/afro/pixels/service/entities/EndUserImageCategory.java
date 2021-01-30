package com.jworks.afro.pixels.service.entities;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
@Table(name = "end_user_image_category", indexes = {
            @Index(name = "IMAGE_CATEGORY_NAME_IDX", columnList = "name"),
            @Index(name = "IS_IMAGE_CATEGORY_ACTIVE_IDX", columnList = "is_active")
})
public class EndUserImageCategory extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "is_active",nullable = false)
    @ColumnDefault("1")
    private boolean isActive;

    @Column
    private String description;

    @OneToMany(mappedBy = "endUserImageCategory", fetch = FetchType.LAZY)
    private List<EndUserImage> endUserImages = new ArrayList<>();
}
