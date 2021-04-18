package com.jworks.afro.pixels.service.enums;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */
public enum Level {
    ONE, TWO, THREE;

    public static boolean contains(String levelParam){
        for (Level level: Level.values()) {
            if (level.name().equals(levelParam)) {
                return true;
            }
        }
        return false;
    }
}
