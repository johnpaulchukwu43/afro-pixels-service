package com.jworks.afro.pixels.service.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "end_user_image_color")
public class EndUserImageColor extends BaseEntity implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "end_user_image_id", referencedColumnName = "id")
    private EndUserImage endUserImage;

    @Column(nullable = false)
    private String color;
}
