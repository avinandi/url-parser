package org.avirup.common.urlparser;

import org.junit.Assert;
import org.junit.Test;

import static org.avirup.common.urlparser.Validators.*;

public class ValidatorTest extends AbstractUrlParserTest {

    @Test
    public void shouldValidateNonEmptyOrNonNull() {
        validateNonEmptyOrNonNull("test", "Should pass");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForEmpty() {
        validateNonEmptyOrNonNull("", "Throw exception");
        Assert.fail("Should throw exception");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNull() {
        validateNonEmptyOrNonNull(null, "Throw exception");
        Assert.fail("Should throw exception");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionIfConditionDoesNotMatch() {
        validate(false, "Throw exception");
        Assert.fail("Should throw exception");
    }

    @Test
    public void shouldValidateCorrectTemplatePattern() {
        validateTemplatePattern("/rest/public/id/{valid_variable}");
    }

    @Test
    public void shouldFailIfTemplateDoesNotStartWithSlash() {
        validateTemplate("rest/public/id/{variable}", IllegalStateException.class, "Template should start with '/'");
    }

    @Test
    public void shouldFailIfTemplateVariableIsBlank() {
        validateTemplate("/rest/public/id/{}", IllegalStateException.class, "Invalid template");
    }

    @Test
    public void shouldFailIfTemplateVariableNameHasInvalidChar() {
        validateTemplate("/rest/public/id/{invalid-char}", IllegalStateException.class, "Invalid template");
    }

    @Test
    public void shouldFailIfTemplateVariableNameHasExtraStartingCurlyBracket() {
        validateTemplate("/rest/public/id/{invalid{char}", IllegalStateException.class, "Invalid template");
    }

    @Test
    public void shouldFailIfTemplateVariableNameHasExtraEndingCurlyBracket() {
        validateTemplate("/rest/public/id/{invalid}char}", IllegalStateException.class, "Invalid template");
    }
}
