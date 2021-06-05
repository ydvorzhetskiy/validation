package com.sabre.gcp.validation;

import lombok.val;
import org.apache.beam.repackaged.core.org.apache.commons.lang3.ArrayUtils;
import org.apache.beam.repackaged.core.org.apache.commons.lang3.StringUtils;
import org.apache.beam.sdk.transforms.DoFn;
import org.reflections.Reflections;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class ValidationChain<T> extends DoFn<T, WithErrors<T>> implements Serializable {

    private final List<BaseValidator<T>> validators;

    private ValidationChain(List<BaseValidator<T>> validators) {
        this.validators = validators;
    }

    static <T> ValidationChain<T> getInstance(
        @SuppressWarnings("SameParameterValue") String validationChainId,
        @SuppressWarnings({"unused", "SameParameterValue"}) Class<T> cl
    ) {
        Reflections reflections = new Reflections("com");
        val validators = reflections.getTypesAnnotatedWith(Validator.class)
            .stream()
            .filter(
                clazz -> Objects.equals(validationChainId, clazz.getAnnotation(Validator.class).validationChainId())
            )
            .sorted((c1, c2) -> {
                if (ArrayUtils.contains(c1.getAnnotation(Validator.class).validateAfter(), c2)) {
                    return 1;
                }
                if (ArrayUtils.contains(c2.getAnnotation(Validator.class).validateAfter(), c1)) {
                    return -1;
                }
                return StringUtils.compare(c1.getName(), c2.getName());
            })
            .map(clazz -> {
                try {
                    //noinspection unchecked
                    return (BaseValidator<T>) clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            })
            .collect(toList());
        return new ValidationChain<>(validators);
    }

    List<BaseValidator<T>> getValidators() {
        return validators;
    }

    @ProcessElement
    public void processElement(@Element T obj, OutputReceiver<WithErrors<T>> out) {
        List<String> errors = validators.stream()
            .flatMap(validator -> validator.doValidation(obj).stream())
            .collect(toList());
        out.output(new WithErrors<>(errors, obj));
    }
}
