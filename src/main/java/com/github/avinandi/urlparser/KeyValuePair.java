package com.github.avinandi.urlparser;

import java.io.Serializable;

public class KeyValuePair<T> implements Serializable {
    private final String key;
    private final Object value;

    KeyValuePair(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public T getValue() {
        return (T) value;
    }
}
