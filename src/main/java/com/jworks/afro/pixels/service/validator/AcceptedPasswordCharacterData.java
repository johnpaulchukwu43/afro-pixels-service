package com.jworks.afro.pixels.service.validator;

import lombok.AllArgsConstructor;
import org.passay.CharacterData;

/**
 * @author Johnpaul Chukwu.
 * @since 24/12/2020
 */

@AllArgsConstructor
public enum AcceptedPasswordCharacterData implements CharacterData {

    MIN_EXPECTED("INSUFFICIENT_SPECIAL", "!#%&*+=?@$&");

    private final String errorCode;
    private final String characters;

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getCharacters() {
        return this.characters;
    }
}
