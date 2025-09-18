package com.example.ProductService.Exception;


import com.example.ProductService.Enum.ErrorCode;

public class ResourceNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object resourceId;

    public ResourceNotFoundException(ErrorCode errorCode, Object resourceId) {
        super(errorCode.name() + " with id [" + resourceId + "] not found");
        this.errorCode = errorCode;
        this.resourceId = resourceId;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object getResourceId() {
        return resourceId;
    }
}



