package com.github.avinandi.urlparser;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.avinandi.urlparser.Sanitizer.decode;
import static com.github.avinandi.urlparser.Sanitizer.sanitizeTemplate;
import static com.github.avinandi.urlparser.Validators.validateNonEmptyOrNonNull;
import static com.github.avinandi.urlparser.Validators.validateTemplatePattern;

public class UrlParser {

    private final TemplateCompiler.CompiledTemplate compiledTemplate;

    private Parse parsed;

    private UrlParser(final TemplateCompiler.CompiledTemplate compiledTemplate) {
        this.compiledTemplate = compiledTemplate;
    }

    public static UrlParser createParser(final String template) {
        validateNonEmptyOrNonNull(template, "Template should not be Null or Empty");
        validateTemplatePattern(template);

        final String sanitizedTemplate = sanitizeTemplate(template);
        TemplateCompiler.CompiledTemplate compiledTemplate = TemplateCompiler.compile(sanitizedTemplate);

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

    public Object getPathParamValue(String variableName, Type type) {
        String input = parsed.getKeyValueMap().get(variableName);
        return type.convert(input);
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
