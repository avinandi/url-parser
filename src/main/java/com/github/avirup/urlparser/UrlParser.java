package com.github.avirup.urlparser;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.avirup.urlparser.Sanitizer.decode;
import static com.github.avirup.urlparser.Sanitizer.sanitizeTemplate;
import static com.github.avirup.urlparser.TemplateCompiler.CompiledTemplate;
import static com.github.avirup.urlparser.TemplateCompiler.compile;
import static com.github.avirup.urlparser.Validators.validateNonEmptyOrNonNull;
import static com.github.avirup.urlparser.Validators.validateTemplatePattern;

public class UrlParser {

    private final CompiledTemplate compiledTemplate;

    private Parse parsed;

    private UrlParser(final CompiledTemplate compiledTemplate) {
        this.compiledTemplate = compiledTemplate;
    }

    public static UrlParser createParser(final String template) {
        validateNonEmptyOrNonNull(template, "Template should not be Null or Empty");
        validateTemplatePattern(template);

        final String sanitizedTemplate = sanitizeTemplate(template);
        CompiledTemplate compiledTemplate = compile(sanitizedTemplate);

        return new UrlParser(compiledTemplate);
    }

    public boolean parse(String url, String decoder) {
        validateNonEmptyOrNonNull(url, "Url cant be Null or Empty");
        String decodedUrl = decode(url, decoder);
        try {
            this.parsed = new Parse(decodedUrl, compiledTemplate);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Malformed Url : ", e);
        }
        return parsed.matches();
    }

    public boolean parse(String url) {
        return parse(url, null);
    }

    public boolean parse(URL url) {
        return parse(url.getFile());
    }

    public String getPathParamValue(String variableName) {
        return parsed.getKeyValueMap().get(variableName);
    }

    public List<String> getQueryParamValue(String queryParamName) {
        return parsed.getQueryParamsMap() == null ? Collections.<String>emptyList()
                : parsed.getQueryParamsMap().get(queryParamName);
    }

    public Map<String, String> getAllPathParams() {
        return parsed.getKeyValueMap();
    }

    public Map<String, List<String>> getAllQueryParams() {
        return parsed.getQueryParamsMap();
    }
}
