package com.letv.portal.validation.Validator;

import com.letv.portal.service.IBaseService;
import com.letv.portal.validation.annotation.IdValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by liuhao1 on 2015/10/28.
 */

public class IdValidValidator extends ApplicationObjectSupport implements ConstraintValidator<IdValid,Object> {

    private String service;
    private long value;

    @Override
    public void initialize(IdValid idValid) {
        this.service = idValid.service();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(null == value) return true; //不判断为空的情况
        if(value instanceof String) {
            this.value = Long.valueOf((String)value);
        } else if(value instanceof Number){
            this.value = ((Number) value).longValue();
        }else {
            return false;
        }

        if(null != ((IBaseService<?>)getApplicationContext().getBean(this.service)).selectById(this.value))
            return true;
        return false;
    }

}
