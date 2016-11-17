package com.github.avinandi.urlparser;

import org.junit.Assert;

import static com.github.avinandi.urlparser.Validators.validateTemplatePattern;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class AbstractUrlParserTest {

    protected void validateTemplate(String pattern, Class<? extends RuntimeException> expectedException, String expectedErrorMessage) {
        try {
            validateTemplatePattern(pattern);
            Assert.fail("Should throw exception");
        } catch (RuntimeException re) {
            assertTrue(expectedException.isInstance(re));
            assertEquals(expectedErrorMessage, re.getMessage());
        }
    }

    protected void assertCreateParserException(String pattern, Class<? extends RuntimeException> expectedException, String expectedErrorMessage) {
        try {
            UrlParser.createParser(pattern);
            Assert.fail("Should throw exception");
        } catch (RuntimeException re) {
            assertTrue(expectedException.isInstance(re));
            assertEquals(expectedErrorMessage, re.getMessage());
        }
    }
}
