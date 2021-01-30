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
@Table(name = "end_user",
        indexes = {
        @Index(name = "IS_USER_ACTIVE_INDEX", columnList = "is_active"),
        @Index(name = "USERNAME_INDEX", columnList = "username"),
        @Index(name = "EMAIL_ADDRESS_INDEX", columnList = "email_address")
})
public class EndUser extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "user_reference", nullable = false, length = 63, unique = true, updatable = false)
    private String userReference;

    @Column(name = "email_address", length = 60, nullable = false, unique = true)
    private String emailAddress;

    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "is_active",nullable = false)
    @ColumnDefault("0")
    private boolean isActive;

    @Column(name = "phone_number", length = 20, unique = true)
    private String phoneNumber;

    @Column(name = "can_upload_images",nullable = false)
    @ColumnDefault("0")
    private boolean canUploadImages;

    @OneToMany(mappedBy = "endUser", fetch = FetchType.LAZY)
    private List<EndUserImage> endUserImages = new ArrayList<>();
}
