package org.avirup.common.urlparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validators {

    private static final Pattern BASIC_TEMPLATE_PATTERN = Pattern.compile(".*?(\\{[\\w]+\\}).*?");
    private static final Pattern PATTERN_RESTRICT_STARTING_CURLY_BRACKET_INSIDE_TEMPLATE_VAR = Pattern.compile("([\\/]\\{[\\w]+\\})");
    private static final Pattern PATTERN_RESTRICT_ENDING_CURLY_BRACKET_INSIDE_TEMPLATE_VAR = Pattern.compile("(\\{[\\w]+\\}[\\/])");

    private Validators() {
        // static class
    }

    static void validateNonEmptyOrNonNull(String input, String message) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    static void validate(boolean isValid, String message) {
        if (isValid) {
            throw new IllegalStateException(message);
        }
    }

    static void validateTemplatePattern(String template) {
        validate(!template.startsWith("/"), "Template should start with '/'");
        validate(!BASIC_TEMPLATE_PATTERN.matcher(template).matches(), "Invalid template");
        int groupsCount1 = getNumberOfFoundGroups(BASIC_TEMPLATE_PATTERN, template);
        int groupsCount2 = getNumberOfFoundGroups(PATTERN_RESTRICT_STARTING_CURLY_BRACKET_INSIDE_TEMPLATE_VAR, template);
        int groupsCount3 = getNumberOfFoundGroups(PATTERN_RESTRICT_ENDING_CURLY_BRACKET_INSIDE_TEMPLATE_VAR, template);
        validate(groupsCount1 == groupsCount2 && groupsCount2 == groupsCount3, "Invalid template");
    }

    private static int getNumberOfFoundGroups(Pattern pattern, String template) {
        Matcher matcher = pattern.matcher(template);
        int counter = 0;
        while (matcher.find()) {
            counter++;
        }
        return counter;
    }
}
