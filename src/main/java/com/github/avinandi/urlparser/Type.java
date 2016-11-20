package com.github.avinandi.urlparser;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

public enum Type {
    INTEGER(Integer.class),
    LONG(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    BOOLEAN(Boolean.class),
    STRING(String.class),
    BIGINT(BigInteger.class),
    BIGDECIMAL(BigDecimal.class),
    ARRAY(Array.class, ",");

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
        return input.split(delimiter.toString());
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
