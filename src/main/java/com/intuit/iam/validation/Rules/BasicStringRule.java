package com.intuit.iam.validation.Rules;


import com.google.common.base.Strings;
import com.intuit.iam.exceptions.ServiceException;

import javax.ws.rs.core.Response;
import java.util.regex.Pattern;

public class BasicStringRule implements Rule<Field>{
    @Override
    public void applyRule(Field field) {
        String fieldValue = (String) field.getField();
        if(Strings.isNullOrEmpty(fieldValue) || !(Pattern.matches(field.getPattern(), fieldValue))) {
            throw new ServiceException(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid input: " + field.getFieldName() + " is null or not matching a pattern");
        }
    }
}
