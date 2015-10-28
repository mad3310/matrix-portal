package com.letv.portal.validation.Validator;

import com.letv.portal.validation.annotation.BooleanLimit;
import org.springframework.context.support.ApplicationObjectSupport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by liuhao1 on 2015/10/28.
 */

public class BooleanLimitValidator extends ApplicationObjectSupport implements ConstraintValidator<BooleanLimit,Boolean> {

    private boolean[] limits;

    @Override
    public void initialize(BooleanLimit limit) {
        this.limits = limit.limits();
    }

    @Override
    public boolean isValid(Boolean value, ConstraintValidatorContext context) {
        if(value == null)
            return false;
        for (int i = 0; i < limits.length; i++) {
            if(value==limits[i]) {
                return true;
            }
        }
        return false;
    }

}
