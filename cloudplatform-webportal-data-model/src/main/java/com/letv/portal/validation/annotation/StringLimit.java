package com.letv.portal.validation.annotation;

import com.letv.portal.validation.Validator.NumberLimitValidator;
import com.letv.portal.validation.Validator.StringLimitValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by liuhao1 on 2015/10/28.
 */

@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = { StringLimitValidator.class })
public @interface StringLimit {

    String [] limits() default {};
    String message() default "value must in limits";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

}
