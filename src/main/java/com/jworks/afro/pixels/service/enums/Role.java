package com.jworks.afro.pixels.service.enums;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */
public enum Role {
    ADMIN, USER;

    public static boolean contains(String userRole){
        for (Role role: Role.values()) {
            if (role.name().equals(userRole)) {
                return true;
            }
        }
        return false;
    }
}
