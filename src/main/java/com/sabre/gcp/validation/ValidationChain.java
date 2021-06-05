package com.sabre.gcp.validation;

import lombok.val;
import org.apache.beam.repackaged.core.org.apache.commons.lang3.ArrayUtils;
import org.apache.beam.repackaged.core.org.apache.commons.lang3.StringUtils;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.vendor.grpc.v1p26p0.org.bouncycastle.util.Arrays;
import org.reflections.Reflections;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ValidationChain<T> extends DoFn<T, WithErrors<T>> {

    private final List<BaseValidator<T>> validators;

    private ValidationChain(List<BaseValidator<T>> validators) {
        this.validators = validators;
    }

    static <T> ValidationChain<T> getInstance(
        @SuppressWarnings("SameParameterValue")
        String validationChainId,
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
            .collect(Collectors.toList());
        return new ValidationChain<>(validators);
    }

    List<BaseValidator<T>> getValidators() {
        return validators;
    }

    //    public static ValidationChain validationChain(String chainName, Class<T> clazz, lambda) {
//        return
//    }


//    @ProcessElement
//    public void processElement(@Element Pnr pnr, OutputReceiver<TableRow> out) {
//        val row = PnrConverter.toTableRow(pnr);
//        out.output(row);
//    }
}
