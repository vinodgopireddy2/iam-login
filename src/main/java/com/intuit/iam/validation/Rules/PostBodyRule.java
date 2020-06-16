package com.intuit.iam.validation.Rules;

import com.google.common.primitives.Booleans;
import com.intuit.iam.exceptions.ServiceException;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

public class PostBodyRule  implements Rule<Field>{
    @Override
    public void applyRule(Field field) {
        Object fieldValue = field.getField();
        boolean[] booleans = {true, false};
        if(fieldValue ==null) {
            throw new ServiceException(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid input: please provide post body");
        }
        for(java.lang.reflect.Field f: fieldValue.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                List<java.lang.reflect.Field> postBodyFields = Arrays.asList(Class.forName("com.intuit.iam.model.requestbody." + field.getPattern()).newInstance().getClass().getDeclaredFields());
                List<String> postBodyFieldNames = (List<String>) CollectionUtils.collect(postBodyFields, new BeanToPropertyValueTransformer("name"));
                if(postBodyFieldNames.contains(f.getName())) {
                    if(f.getType() == List.class)
                        continue;
                    if(f.getType() == String.class ? (f.get(fieldValue) == null) : !Booleans.contains(booleans, (Boolean) f.get(fieldValue)))
                    {
                        throw new ServiceException(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid input: " + f.getName() + " attribute is missing in post body");
                    }
                }
            }
            catch (Exception e) {
                throw new ServiceException(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid input: " + f.getName() + " attribute is missing in post body");
            }
        }
    }
}
