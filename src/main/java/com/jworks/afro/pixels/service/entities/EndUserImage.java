package com.jworks.afro.pixels.service.entities;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.search.engine.backend.types.TermVector;
import org.hibernate.search.engine.spatial.GeoPoint;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 *
 *
 * imageType
 * width
 * height
 * colors
 */

@Entity
@Data
@Builder
@Indexed
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

    @FullTextField(termVector = TermVector.YES)
    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.EAGER)
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @JoinColumn(name = "end_user_image_category_id", referencedColumnName = "id")
    private EndUserImageCategory endUserImageCategory;

    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    private EndUser endUser;

    @FullTextField(termVector = TermVector.YES)
    @Column(length = 150)
    private String tag;

    @FullTextField(termVector = TermVector.YES)
    @Column(nullable = false)
    private String description;

    @GenericField
    @ColumnDefault("0")
    @Column(name = "is_active",nullable = false)
    private boolean isActive;

    @Embedded
    @IndexedEmbedded
    private MetaData metaData;


    @Data
    @Builder
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetaData implements Serializable {

        private static final long serialVersionUID = 1L;

        @KeywordField
        @Column(length = 200, nullable = false)
        private String imageUrl;

        @KeywordField
        @Column(length = 10, nullable = false)
        private String imageFileFormat;

        @GenericField
        @Column(nullable = false)
        private Integer imageWidth;

        @GenericField
        @Column(nullable = false)
        private Integer imageHeight;

        @IndexedEmbedded
        @OneToMany(mappedBy = "endUserImage")
        private List<EndUserImageColor> imageColors;
    }
}
