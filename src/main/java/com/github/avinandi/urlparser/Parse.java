package com.github.avinandi.urlparser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;

class Parse {
    private final Matcher decodedUrlMatcher;
    private final Map<String, String> keyValueMap;
    private final Map<String, List<String>> queryParamsMap;

    Parse(String decodedUrl, TemplateCompiler.CompiledTemplate compiledTemplate) throws URISyntaxException {
        URI tempUri = new URI(decodedUrl);
        this.decodedUrlMatcher = compiledTemplate.urlPattern.matcher(tempUri.getPath());
        this.queryParamsMap = parseQueryParams(tempUri);
        if (matches()) {
            this.keyValueMap = parseWithRegEx(compiledTemplate);
        } else {
            this.keyValueMap = new HashMap<>();
        }
    }

    private Map<String, List<String>> parseQueryParams(URI uri) {
        final String queryString = uri.getQuery();
        if (queryString == null) {
            return null;
        }
        final String[] pairs = queryString.split("&");
        final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? Sanitizer.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, new LinkedList<String>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? Sanitizer.decode(pair.substring(idx + 1), "UTF-8") : null;
            query_pairs.get(key).add(value);
        }
        return query_pairs;
    }

    public boolean matches() {
        return decodedUrlMatcher.matches();
    }

    Map<String, String> getKeyValueMap() {
        return keyValueMap;
    }

    Map<String, List<String>> getQueryParamsMap() {
        return queryParamsMap;
    }

    private Map<String, String> parseWithRegEx(TemplateCompiler.CompiledTemplate compiledTemplate) {
        Map<String, String> tempkeyValMap = new HashMap<>();
        Iterator<String> templateVariables = compiledTemplate.templateVariables.iterator();
        int index  = 0;
        while (templateVariables.hasNext()) {
            String varName = templateVariables.next();
            String value = decodedUrlMatcher.group(++index);
            tempkeyValMap.put(varName, value);
        }
        return tempkeyValMap;
    }
}
