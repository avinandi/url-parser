package org.avirup.common.urlparser;

import org.junit.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.avirup.common.urlparser.UrlParser.createParser;
import static org.junit.Assert.*;

/**
 * Check more failure condition in ValidatorTest
 */
public class UrlParserTest extends AbstractUrlParserTest {

    @Test
    public void shouldParseUrlPathParams() {
        UrlParser urlParser = createParser("/rest/public/username/{username}/pin/{pin}");
        assertTrue(urlParser.parse("/rest/public/username/gr8Avi/pin/4321"));

        String username = urlParser.getPathParamValue("username");
        String pin = urlParser.getPathParamValue("pin");
        assertEquals("gr8Avi", username);
        assertEquals("4321", pin);

        Map<String, String> expectedPathParams = new HashMap<>();
        expectedPathParams.put("username", "gr8Avi");
        expectedPathParams.put("pin", "4321");

        Map<String, String> pathParams = urlParser.getAllPathParams();
        assertEquals(expectedPathParams, pathParams);
    }

    @Test
    public void shouldParseUrlQueryParams() {
        UrlParser urlParser = createParser("/rest/public/username/{username}/pin/{pin}");
        assertTrue(urlParser.parse("/rest/public/username/gr8Avi/pin/4321?isPasswordSet=true"));
        List<String> queryParamVal = urlParser.getQueryParamValue("isPasswordSet");
        assertEquals(Arrays.asList("true"), queryParamVal);
    }

    @Test
    public void shouldParseUrlMultipleQueryParams() {
        UrlParser urlParser = createParser("/rest/public/username/{username}/pin/{pin}");
        assertTrue(urlParser.parse("/rest/public/username/gr8Avi/pin/4321?contract=1234&contract=4567&isSecure=true"));

        List<String> contracts = urlParser.getQueryParamValue("contract");
        List<String> isSecure = urlParser.getQueryParamValue("isSecure");
        assertEquals(Arrays.asList("1234", "4567"), contracts);
        assertEquals(Arrays.asList("true"), isSecure);

        Map<String, List<String>> expectedQueryParams = new HashMap<>();
        expectedQueryParams.put("contract", Arrays.asList("1234", "4567"));
        expectedQueryParams.put("isSecure", Arrays.asList("true"));

        Map<String, List<String>> queryParams = urlParser.getAllQueryParams();
        assertEquals(expectedQueryParams, queryParams);
    }

    @Test
    public void shouldParseUrlPathParamsWithInputAsUrl() throws Exception {
        UrlParser urlParser = createParser("/rest/public/username/{username}/pin/{pin}");
        assertTrue(urlParser.parse(new URL("https://domain.do/rest/public/username/gr8Avi/pin/4321")));

        Map<String, String> expectedPathParams = new HashMap<>();
        expectedPathParams.put("username", "gr8Avi");
        expectedPathParams.put("pin", "4321");

        Map<String, String> pathParams = urlParser.getAllPathParams();
        assertEquals(expectedPathParams, pathParams);
    }

    @Test
    public void shouldParseUrlPathParamsWithEncoderInput() throws Exception {
        UrlParser urlParser = createParser("/rest/public/username/{username}/pin/{pin}");
        assertTrue(urlParser.parse("https://domain.do/rest/public/username/gr8Avi/pin/4321", "UTF-8"));

        Map<String, String> expectedPathParams = new HashMap<>();
        expectedPathParams.put("username", "gr8Avi");
        expectedPathParams.put("pin", "4321");

        Map<String, String> pathParams = urlParser.getAllPathParams();
        assertEquals(expectedPathParams, pathParams);
    }

    @Test
    public void shouldThrowExceptionForDuplicatePathVariables() {
        assertCreateParserException("/rest/public/username/{username}/pin/{username}",
                IllegalStateException.class, "Duplicate occurrence of variable {username}");
    }

    @Test
    public void shouldReturnFalseIfPatternDoesNotMatch() {
        UrlParser urlParser = createParser("/rest/public/username/{username}/pin/{pin}");
        assertFalse(urlParser.parse("/rest/public/username/gr8Avi/pin/4321/extra"));
    }

    @Test
    public void shouldFailForMalformedUrl() {
        UrlParser urlParser = createParser("/rest/public/username/{username}/pin/{pin}");
        try {
            urlParser.parse("/rest/{public}/username/gr8Avi/pin/4321");
            fail("Should throw exception");
        } catch (RuntimeException ex) {
            assertEquals(IllegalArgumentException.class, ex.getClass());
            assertTrue(ex.getCause().toString().contains(URISyntaxException.class.getCanonicalName()));
        }
    }
}
