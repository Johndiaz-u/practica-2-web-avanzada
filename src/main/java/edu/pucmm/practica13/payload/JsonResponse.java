package edu.pucmm.practica13.payload;

import java.util.Set;

public class JsonResponse {
    private Boolean hasError;
    private String additionalErrors;
    private Integer code;
    private String message;
    private Set<String> errors;
    private Object data;

    public JsonResponse(Object data, String message, String additionalErrors, Set<String> errors, Integer code) {
        this.data = data;
        this.message = message;
        this.additionalErrors = additionalErrors;
        this.hasError = this.hasError(code);
        this.code = code;
        this.errors = errors;
    }

    public Boolean getHasError() {
        return hasError;
    }

    public JsonResponse setHasError(Boolean hasError) {
        this.hasError = hasError;
        return this;
    }

    public String getAdditionalErrors() {
        return additionalErrors;
    }

    public JsonResponse setAdditionalErrors(String additionalErrors) {
        this.additionalErrors = additionalErrors;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public JsonResponse setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public JsonResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public Set<String> getErrors() {
        return errors;
    }

    public JsonResponse setErrors(Set<String> errors) {
        this.errors = errors;
        return this;
    }

    public Object getData() {
        return data;
    }

    public JsonResponse setData(Object data) {
        this.data = data;
        return this;
    }

    private boolean hasError(Integer code) {
        if (code >= 200 && code <= 299) {
            return false;
        }
        if (code >= 300 && code <= 399) {
            return false;
        }
        if (code >= 400 && code <= 499) {
            return true;
        }
        if (code >= 500 && code <= 599) {
            return true;
        }
        return true;
    }
}