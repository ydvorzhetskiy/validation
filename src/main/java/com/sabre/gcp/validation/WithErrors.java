package com.sabre.gcp.validation;

import org.apache.beam.sdk.values.POutput;

import java.io.Serializable;
import java.util.List;

public class WithErrors<T> implements Serializable {

    private final List<String> errors;
    private final T object;

    public WithErrors(List<String> errors, T object) {
        this.errors = errors;
        this.object = object;
    }

    public List<String> getErrors() {
        return errors;
    }

    public T getObject() {
        return object;
    }

    public boolean isValid() {
        return errors == null || errors.isEmpty();
    }
}
