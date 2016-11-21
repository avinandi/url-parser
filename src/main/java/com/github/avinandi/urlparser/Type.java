package com.github.avinandi.urlparser;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

import static com.github.avinandi.urlparser.Validators.validateNonEmptyOrNonNull;

public enum Type {
    INTEGER(Integer.class),
    LONG(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    BOOLEAN(Boolean.class),
    STRING(String.class),
    BIGINT(BigInteger.class),
    BIGDECIMAL(BigDecimal.class),
    ARRAY(Array.class, TypeConstant.DEFAULT_DELIM);

    final Class<?> clazz;
    CharSequence delimiter;

    Type(Class<?> clazz) {
        this(clazz, null);
    }

    Type(Class<?> clazz, CharSequence cs) {
        this.clazz = clazz;
        this.delimiter = cs;
    }

    public Type setDelimiterForTypeArray(CharSequence delimiter) {
        validateNonEmptyOrNonNull(delimiter.toString(), "Delimiter can't be null or empty");
        this.delimiter = delimiter;
        return this;
    }

    public Object convert(String input) {
        switch (this) {
            case ARRAY:
                return splitStringByDelimiter(input);
            case INTEGER:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case BIGINT:
            case BIGDECIMAL:
            case BOOLEAN:
                return handleGenericTypes(input);
            case STRING:
                return input;
            default:
                throw new ClassCastException("Unhandled type " + this);

        }
    }

    private String[] splitStringByDelimiter(String input) {
        CharSequence customDelim = delimiter;
        this.setDelimiterForTypeArray(TypeConstant.DEFAULT_DELIM); // After operation reset to default DELIMITER
        return input.split(customDelim.toString());
    }

    private Object handleGenericTypes(String input) {
        Class<?> cls = this.clazz;
        try {
            return cls.getConstructor(String.class).newInstance(input);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new ClassCastException("Can't convert String " + input + " to number type "
                    + this.clazz + "with inline error: " + e.getMessage());
        }
    }
}

class TypeConstant {
    static final CharSequence DEFAULT_DELIM = ",";
}
