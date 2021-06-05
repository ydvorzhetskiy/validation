package com.sabre.gcp.validation;

public interface BaseValidator<T> {

    boolean doValidation(T validationObject);
}
