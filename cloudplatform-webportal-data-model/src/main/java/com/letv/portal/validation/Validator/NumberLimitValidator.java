package com.letv.portal.validation.Validator;

import com.letv.portal.validation.annotation.NumberLimit;
import org.springframework.context.support.ApplicationObjectSupport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by liuhao1 on 2015/10/28.
 */

public class NumberLimitValidator extends ApplicationObjectSupport implements ConstraintValidator<NumberLimit, Number> {

    private long[] limits;

    @Override
    public void initialize(NumberLimit limit) {
        this.limits = limit.limits();
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if(value == null)
            return false;
        for (int i = 0; i < limits.length; i++) {
            if(value.longValue()==limits[i]) {
                return true;
            }
        }
        return false;
    }

}
