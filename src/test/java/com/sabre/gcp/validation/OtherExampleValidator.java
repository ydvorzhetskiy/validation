package com.sabre.gcp.validation;

import java.util.List;

@Validator(
    validationChainId = "otherChain"
)
public class OtherExampleValidator implements BaseValidator<Object> {
    @Override
    public List<String> doValidation(Object object) {
        return null;
    }
}
