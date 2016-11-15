package com.github.avirup.urlparser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SanitizerTest extends AbstractUrlParserTest {

    @Test
    public void shouldRemoveQueryStringFromTemplate() {
        String sanitizedTemplate = Sanitizer.sanitizeTemplate("/rest/public/id/{valid_variable}?something=1&something=2");
        assertEquals("/rest/public/id/{valid_variable}", sanitizedTemplate);
    }

    @Test
    public void shouldRemoveEndingSlashOfTemplate() {
        String sanitizedTemplate = Sanitizer.sanitizeTemplate("/rest/public/id/{valid_variable}/?dsds=1");
        assertEquals("/rest/public/id/{valid_variable}", sanitizedTemplate);
    }

    @Test
    public void shouldReturnBlankForOnlySlash() {
        String sanitizedTemplate = Sanitizer.sanitizeTemplate("/");
        assertEquals("", sanitizedTemplate);
    }

    @Test
    public void shouldDecodeUrlWithEncodingName() {
        String sanitizedTemplate = Sanitizer.decode("%2Fmysite.com%2Fhome.htm%3Findex%3D0", "UTF-8");
        assertEquals("/mysite.com/home.htm?index=0", sanitizedTemplate);
    }

    @Test
    public void shouldDecodeUrlWithUTF8WithoutAnySpecificEncoding() {
        String sanitizedTemplate = Sanitizer.decode("%2Fmysite.com%2Fhome.htm%3Findex%3D0", null);
        assertEquals("/mysite.com/home.htm?index=0", sanitizedTemplate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfEncodingIsWrong() {
        Sanitizer.decode("%2Fmysite.com%2Fhome.htm%3Findex%3D0", "Something");
        fail("Exception expected");
    }
}
