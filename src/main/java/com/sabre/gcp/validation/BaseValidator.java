package com.sabre.gcp.validation;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseValidator<T> extends Serializable {

    /**
     * Validates object
     * @param object object to validate
     * @return list of error messages if object is not valid, empty list otherwise
     */
    List<String> doValidation(T object);
}
