package com.jworks.afro.pixels.service.validator;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.passay.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Johnpaul Chukwu.
 * @since 24/12/2020
 */

@Slf4j
@Service
public class PasswordValidator implements ConstraintValidator<AcceptedPasswordFormat, String> {

    private static final List<CharacterRule> passwordRules = new ArrayList();

    @Value("${minimum.password.length:7}")
    private Integer minPasswordLength;

    @Value("${maximum.password.length:30}")
    private Integer maxPasswordLength;

    @Value("${minimum.password.uppercase.length:1}")
    private Integer minNumOfUppercase;

    @Value("${minimum.password.digit.length:1}")
    private Integer minPasswordDigitLength;

    @Value("${minimum.special.character.length:1}")
    private Integer minNumOfSpecialCharacters;

    private String message;

    private static int mPasswordLength;

    @PostConstruct
    public void init() {
        mPasswordLength = this.minPasswordLength;
        if (this.minNumOfUppercase > 0) {
            passwordRules.add(new CharacterRule(EnglishCharacterData.UpperCase, this.minNumOfUppercase));
        }

        if (this.minPasswordDigitLength > 0) {
            passwordRules.add(new CharacterRule(EnglishCharacterData.Digit, this.minPasswordDigitLength));
        }

        if (this.minNumOfSpecialCharacters > 0) {
            passwordRules.add(new CharacterRule(AcceptedPasswordCharacterData.MIN_EXPECTED , this.minNumOfSpecialCharacters));
        }
    }

    @Override
    public void initialize(AcceptedPasswordFormat constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }


    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null) {
            return false;
        } else {
            List<Rule> validationRules = new ArrayList();
            validationRules.add(new LengthRule(this.minPasswordLength, this.maxPasswordLength));
            validationRules.add(new WhitespaceRule());
            validationRules.addAll(passwordRules);
            org.passay.PasswordValidator validator = new org.passay.PasswordValidator(validationRules);
            RuleResult result = validator.validate(new PasswordData(password));
            if (result.isValid()) {
                return true;
            } else {
                constraintValidatorContext.disableDefaultConstraintViolation();
                if (!StringUtils.isEmpty(this.message)) {
                    constraintValidatorContext.buildConstraintViolationWithTemplate(this.message).addConstraintViolation();
                } else {
                    constraintValidatorContext.buildConstraintViolationWithTemplate(Joiner.on("\n ").join(validator.getMessages(result))).addConstraintViolation();
                }

                return false;
            }
        }
    }




}
