package org.sapl.commons.controller;

import org.sapl.commons.exception.ApiException;
import org.sapl.commons.exception.NotFoundApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ErrorSupportController implements ErrorController {

    private final static String PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = PATH)
    ApiException.Json error(WebRequest request, HttpServletResponse response) {

        Throwable error = errorAttributes.getError(request);

        if (error != null && error instanceof NotFoundApiException) response.setStatus(HttpStatus.NOT_FOUND.value());

        if (error != null && error instanceof ApiException) return ((ApiException) error).toJson();

        if (response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value() || error == null) {
            if (error != null) onInternalError(error);
            return new ApiException(response.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).toJson();
        }

        return new ApiException(response.getStatus(), error.getMessage()).toJson();
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    protected void onInternalError(Throwable throwable) {

    }

}