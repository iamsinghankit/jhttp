package com.singhankit.jhttp;

/**
 * @author Ankit Singh
 */
public enum MediaType {

    APPLICATION_JSON("application/json"),
    APPLICATION_XML("application/xml"),
    TEXT("text/plain");

    private final String value;

    MediaType(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public boolean equalCheck(String value) {
        return this.value.equals(value);
    }
}
