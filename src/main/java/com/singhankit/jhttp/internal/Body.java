package com.singhankit.jhttp.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ankitsingh
 */
public class Body {
    private final Map<String, String> formFields = new HashMap<>();
    private final Map<String, FilePart> fileFields = new HashMap<>();
    private String raw;
    private String contentType;

    public Map<String, String> getFormFields() {
        return formFields;
    }

    public Map<String, FilePart> getFileFields() {
        return fileFields;
    }

    public String getRaw() {
        return raw;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getField(String key) {
        return formFields.get(key);
    }

    public FilePart getFile(String key) {
        return fileFields.get(key);
    }

    public boolean isMultipart() {
        return contentType != null && contentType.startsWith("multipart/form-data");
    }

    public boolean isUrlEncoded() {
        return contentType != null && contentType.startsWith("application/x-www-form-urlencoded");
    }

    public boolean isRaw() {
        return contentType == null || (!isMultipart() && !isUrlEncoded());
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public record FilePart(String filename, byte[] content) {}

}
