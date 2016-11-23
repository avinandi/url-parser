package com.github.avinandi.urlparser;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Check more failure condition in ValidatorTest
 */
public class UrlParserTest extends AbstractUrlParserTest {

    @Test
    public void shouldParseUrlPathParams() {
        UrlParser urlParser = UrlParser.createParser("/rest/public/username/{username}/pin/{pin}");
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
    public void shouldParseUrlPathParamsWithoutAnyStaticPathSpecifier() {
        UrlParser urlParser = UrlParser.createParser("/{useridid}/{username}");
        assertTrue(urlParser.parse("/gr8Avi/Avirup"));

        String useridid = urlParser.getPathParamValue("useridid");
        String username = urlParser.getPathParamValue("username");
        assertEquals("gr8Avi", useridid);
        assertEquals("Avirup", username);
    }

    @Test
    public void shouldParseUrlQueryParams() {
        UrlParser urlParser = UrlParser.createParser("/rest/public/username/{username}/pin/{pin}");
        assertTrue(urlParser.parse("/rest/public/username/gr8Avi/pin/4321?isPasswordSet=true"));
        List<String> queryParamVal = urlParser.getQueryParamValue("isPasswordSet");
        assertEquals(Arrays.asList("true"), queryParamVal);
    }

    @Test
    public void shouldParseUrlMultipleQueryParams() {
        UrlParser urlParser = UrlParser.createParser("/rest/public/username/{username}/pin/{pin}");
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
        UrlParser urlParser = UrlParser.createParser("/rest/public/username/{username}/pin/{pin}");
        assertTrue(urlParser.parse(new URL("https://domain.do/rest/public/username/gr8Avi/pin/4321")));

        Map<String, String> expectedPathParams = new HashMap<>();
        expectedPathParams.put("username", "gr8Avi");
        expectedPathParams.put("pin", "4321");

        Map<String, String> pathParams = urlParser.getAllPathParams();
        assertEquals(expectedPathParams, pathParams);
    }

    @Test
    public void shouldParseUrlPathParamsWithEncoderInput() throws Exception {
        UrlParser urlParser = UrlParser.createParser("/rest/public/username/{username}/pin/{pin}");
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
        UrlParser urlParser = UrlParser.createParser("/rest/public/username/{username}/pin/{pin}");
        assertFalse(urlParser.parse("/rest/public/username/gr8Avi/pin/4321/extra"));
    }

    @Test
    public void shouldFailForMalformedUrl() {
        UrlParser urlParser = UrlParser.createParser("/rest/public/username/{username}/pin/{pin}");
        try {
            urlParser.parse("/rest/{public}/username/gr8Avi/pin/4321");
            fail("Should throw exception");
        } catch (RuntimeException ex) {
            assertEquals(IllegalArgumentException.class, ex.getClass());
            assertTrue(ex.getCause().toString().contains(URISyntaxException.class.getCanonicalName()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForWrongVariableName() {
        UrlParser urlParser = UrlParser.createParser("/rest/public/pin/{pin}");
        assertTrue(urlParser.parse("/rest/public/pin/4321"));
        urlParser.getPathParamValue("pintemp");
    }

    @Test
    public void shouldGetIntegerType() {
        UrlParser urlParser = UrlParser.createParser("/rest/public/pin/{pin}");
        assertTrue(urlParser.parse("/rest/public/pin/4321"));

        Integer pin = (Integer) urlParser.getPathParamValue("pin", Type.INTEGER);
        assertEquals(4321, pin.intValue());
    }

    @Test
    public void shouldGetLongType() {
        UrlParser urlParser = UrlParser.createParser("/rest/public/pin/{pin}");
        assertTrue(urlParser.parse("/rest/public/pin/4321"));

        Long pin = (Long) urlParser.getPathParamValue("pin", Type.LONG);
        assertEquals(4321, pin.longValue());
    }

    @Test
    public void shouldGetDoubleType() {
        UrlParser urlParser = UrlParser.createParser("/rest/public/price/{price}");
        assertTrue(urlParser.parse("/rest/public/price/43.21"));

        Double price = (Double) urlParser.getPathParamValue("price", Type.DOUBLE);
        assertEquals(43.21, price.doubleValue(), 1e-15);
    }

    @Test
    public void shouldGetFloatType() {
        UrlParser urlParser = UrlParser.createParser("/rest/public/price/{price}");
        assertTrue(urlParser.parse("/rest/public/price/43.21"));

        Float price = (Float) urlParser.getPathParamValue("price", Type.FLOAT);
        assertEquals(43.21, price.floatValue(), 0.0002);
    }

    @Test
    public void shouldGetBigIntType() {
        UrlParser urlParser = UrlParser.createParser("/rest/public/price/{longpin}");
        assertTrue(urlParser.parse("/rest/public/price/434343434343434343434343"));

        BigInteger longpin = (BigInteger) urlParser.getPathParamValue("longpin", Type.BIGINT);
        assertEquals(new BigInteger("434343434343434343434343"), longpin);
    }

    @Test
    public void shouldGetBigDecimalType() {
        UrlParser urlParser = UrlParser.createParser("/rest/public/price/{longprice}");
        assertTrue(urlParser.parse("/rest/public/price/43.4343434343434343434343"));

        BigDecimal longprice = (BigDecimal) urlParser.getPathParamValue("longprice", Type.BIGDECIMAL);
        assertEquals(new BigDecimal("43.4343434343434343434343"), longprice);
    }

    @Test
    public void shouldGetBooleanType() {
        UrlParser urlParser = UrlParser.createParser("/rest/public/price/{isFree}");
        assertTrue(urlParser.parse("/rest/public/price/true"));

        boolean isFree = (boolean) urlParser.getPathParamValue("isFree", Type.BOOLEAN);
        assertEquals(true, isFree);
    }

    @Test
    public void shouldGetArrayTypeWithDefaultDelimiter() {
        UrlParser urlParser = UrlParser.createParser("/rest/public/usernames/{usernames}");
        assertTrue(urlParser.parse("/rest/public/usernames/Ram,Sham,Jadu,Madhu"));

        String[] usernames = (String[]) urlParser.getPathParamValue("usernames", Type.ARRAY);
        String[] expectedUsernames = {"Ram", "Sham", "Jadu", "Madhu"};
        assertArrayEquals(expectedUsernames, usernames);
    }

    @Test
    public void shouldGetArrayTypeWithCustomDelimiter() {
        UrlParser urlParser = UrlParser.createParser("/rest/public/usernames/{usernames}");
        assertTrue(urlParser.parse("/rest/public/usernames/Ram:Sham:Jadu:Madhu"));

        Type arrayTypeWithCustomDelem = Type.ARRAY.setDelimiterForTypeArray(":");
        String[] usernames = (String[]) urlParser.getPathParamValue("usernames", arrayTypeWithCustomDelem);
        String[] expectedUsernames = {"Ram", "Sham", "Jadu", "Madhu"};
        assertArrayEquals(expectedUsernames, usernames);
    }

    @Test(expected = ClassCastException.class)
    public void shouldFailForWrongType() {
        UrlParser urlParser = UrlParser.createParser("/rest/public/usernames/{username}");
        assertTrue(urlParser.parse("/rest/public/usernames/Avirup"));

        urlParser.getPathParamValue("username", Type.INTEGER);
    }
}
