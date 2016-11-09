package org.avirup.common.urlparser;

import java.lang.reflect.Array;

public enum Type {
    INTEGER(Integer.class),
    LONG(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    BOOLEAN(Boolean.class),
    STRING(String.class),
    ARRAY(Array.class);

    final Class<?> clazz;

    Type(Class<?> clazz) {
        this.clazz = clazz;
    }
}
