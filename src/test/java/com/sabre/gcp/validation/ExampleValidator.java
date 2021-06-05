package com.sabre.gcp.validation;

import java.util.List;

@Validator(
    validationChainId = "myChain"
)
public class ExampleValidator implements BaseValidator<Object> {
    @Override
    public List<String> doValidation(Object object) {
        return null;
    }
}
