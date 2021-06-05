package com.sabre.gcp.validation;

import java.util.List;
import java.util.Optional;

public interface BaseValidator<T> {

    /**
     * Validates object
     * @param object object to validate
     * @return list of error messages if object is not valid, empty list or null otherwise
     */
    List<String> doValidation(T object);
}
