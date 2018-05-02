package org.sapl.commons.exception;


import java.io.Serializable;

public class NotFoundApiException extends ApiException implements Serializable {


    public NotFoundApiException() {
        super(404, "Resource not found");
    }

}
