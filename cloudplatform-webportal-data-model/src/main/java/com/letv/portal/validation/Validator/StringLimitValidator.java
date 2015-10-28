package com.letv.portal.validation.Validator;

import com.letv.portal.validation.annotation.StringLimit;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.ApplicationObjectSupport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by liuhao1 on 2015/10/28.
 */

public class StringLimitValidator extends ApplicationObjectSupport implements ConstraintValidator<StringLimit, String> {

    private String[] limits;

    @Override
    public void initialize(StringLimit limit) {
        this.limits = limit.limits();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(StringUtils.isBlank(value))
            return false;
        for (int i = 0; i < limits.length; i++) {
            if(limits[i].equals(value)) {
                return true;
            }
        }
        return false;
    }

}
