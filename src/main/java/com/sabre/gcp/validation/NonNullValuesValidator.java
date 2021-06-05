package com.sabre.gcp.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class NonNullValuesValidator<T> implements BaseValidator<T> {

    private transient List<Field> fields;

    @Override
    public final List<String> doValidation(T object) {
        Class<?> clazz = object.getClass();
        List<Field> fields = extractFields(clazz);
        List<String> errors = new ArrayList<>();
        for (Field field : fields) {
            try {
                Object value = field.get(object);
                if (value == null) {
                    errors.add("Field " + field.getName() + " is null");
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Can't access field " + clazz.getName() + "." + field.getName());
            }
        }
        return errors;
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
