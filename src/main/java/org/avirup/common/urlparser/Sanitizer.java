package org.avirup.common.urlparser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Sanitizer {

    private Sanitizer() {
        //static class
    }

    static String sanitizeTemplate(final String template) {
        int templateLength = template.length();
        String sanitizedTemplate = removeEndSlash(template, templateLength);
        sanitizedTemplate = removeQueryStringTemplating(sanitizedTemplate);
        return sanitizedTemplate;
    }

    private static String removeEndSlash(final String template, final int templateLength) {
        if (template.endsWith("/")) {
            return template.substring(0, templateLength);
        }
        return template;
    }

    private static String removeQueryStringTemplating(final String template) {
        if (template.indexOf("?") >= 0) {
            return template.substring(0, template.indexOf("?"));
        }
        return template;
    }

    static String decode(String urlOrParam, String decoder) {
        String dec = decoder;
        try {
            dec = decoder == null || decoder.isEmpty() ? "UTF-8" : decoder;
            return URLDecoder.decode(urlOrParam, dec);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(String.format("Decoding failed for %s with decoder %s", urlOrParam, dec));
        }
    }
}
