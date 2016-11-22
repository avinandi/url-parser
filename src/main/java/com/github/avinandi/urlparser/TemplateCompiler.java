package com.github.avinandi.urlparser;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class TemplateCompiler {

    private static final String VALUE_PATTERN_STRING = "([^/]+?)";
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("(\\{[\\w]+\\})");
    private static final Pattern VARIABLE_GROUP_PATTERN = Pattern.compile("^\\{([\\w]+)\\}$");

    static CompiledTemplate compile(final String template) {
        Matcher matcher = VARIABLE_PATTERN.matcher(template);
        Set<String> variableNames = getTemplateVariableNames(matcher);
        String replacedByValuePattern = matcher.replaceAll(VALUE_PATTERN_STRING); // This is mutable, so once you replace this wont be able to get variable names
        return new CompiledTemplate(template,
                Pattern.compile(replacedByValuePattern),
                variableNames);
    }

    static Set<String> getTemplateVariableNames(Matcher matcher) {
        Set<String> templateVariables = new LinkedHashSet<>(); // LinkedHashSet is used to avoid Duplicate entries and maintain insertion order
        while (matcher.find()) {
            String group = matcher.group();
            Matcher groupMatcher = VARIABLE_GROUP_PATTERN.matcher(matcher.group());
            Validators.validate(groupMatcher.matches(), format("Variable pattern %s is not valid", group));
            Validators.validate(templateVariables.add(groupMatcher.group(1)), format("Duplicate occurrence of variable %s", group));
        }
        return templateVariables;
    }

    static class CompiledTemplate {
        final String template;
        final Pattern urlPattern;
        final Set<String> templateVariables;

        CompiledTemplate(String template, Pattern urlPattern, Set<String> templateVariables) {
            this.template = template;
            this.urlPattern = urlPattern;
            this.templateVariables = templateVariables;
        }
    }
}
