package com.letv.portal.validation.annotation;

import com.letv.portal.validation.Validator.BooleanLimitValidator;
import com.letv.portal.validation.Validator.IdValidValidator;

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
@Constraint(validatedBy = { IdValidValidator.class })
public @interface IdValid {

    String message() default "id invalid";

    String service() default "baseService";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
