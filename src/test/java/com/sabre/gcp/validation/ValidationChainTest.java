package com.sabre.gcp.validation;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValidationChainTest {

    @Test
    public void testTestValidators() {
        val validators =
            ValidationChain.getInstance("myChain", Object.class);
        assertTrue(validators.getValidators().get(0) instanceof ExampleValidator);
    }
}
