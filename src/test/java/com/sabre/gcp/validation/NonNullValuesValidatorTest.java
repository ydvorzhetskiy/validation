package com.sabre.gcp.validation;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class NonNullValuesValidatorTest {

    private static class ClassExample {
        private String fieldExample;
    }

    private static class ExampleNonNullValidator extends NonNullValuesValidator<ClassExample> {

        @Override
        protected List<String> getNonNullFieldNames() {
            return singletonList("fieldExample");
        }
    }

    @Test
    public void testValidNonNullField() {
        ClassExample obj = new ClassExample();
        obj.fieldExample = "Non-null value";

        NonNullValuesValidator<ClassExample> validator = new ExampleNonNullValidator();
        assertTrue(validator.doValidation(obj).isEmpty());
    }

    @Test
    public void testInvalidNullField() {
        ClassExample obj = new ClassExample();
        assert obj.fieldExample == null;

        NonNullValuesValidator<ClassExample> validator = new ExampleNonNullValidator();
        assertFalse(validator.doValidation(obj).isEmpty());
    }
}
