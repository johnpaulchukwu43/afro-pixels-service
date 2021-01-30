package com.jworks.afro.pixels.service.validator;

/**
 * @author Johnpaul Chukwu.
 * @since 24/12/2020
 */

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {PasswordValidator.class}
)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AcceptedPasswordFormat {
    String message() default"";

    Class<?>[]groups()default{};

    Class<?extends Payload>[]payload()default{};
}
