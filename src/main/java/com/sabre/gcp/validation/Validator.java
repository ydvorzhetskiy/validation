package com.sabre.gcp.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Validator {

    String validationChainId();

    Class<? extends BaseValidator<?>>[] validateAfter() default {};

    String[] validationFields() default {};
}
