package com.sabre.gcp.validation;

import java.lang.reflect.Field;
import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class NonNullValuesValidator<T> implements BaseValidator<T> {

    private List<Field> fields;

    @Override
    public final boolean doValidation(T validationObject) {
        Class<?> clazz = validationObject.getClass();
        List<Field> fields = extractFields(clazz);
        for (Field field : fields) {
            try {
                Object value = field.get(validationObject);
                if (value == null) return false;
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Can't access field " + clazz.getName() + "." + field.getName());
            }
        }
        return true;
    }

    abstract protected List<String> getNonNullFieldNames();

    private synchronized List<Field> extractFields(Class<?> clazz) {
        if (fields != null) return fields;
        fields = this.getNonNullFieldNames().stream()
            .map(fieldName -> {
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    return field;
                } catch (NoSuchFieldException e) {
                    throw new IllegalStateException("Can't find field " + clazz.getName() + "." + fieldName);
                }
            })
            .collect(toList());
        return fields;
    }
}
