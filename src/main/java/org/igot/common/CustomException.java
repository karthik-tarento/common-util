package org.igot.common;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class CustomException extends RuntimeException {
    private String code;
    private String message;
    private HttpStatus httpStatusCode;

    public CustomException() {
    }

    public CustomException(String code, String message, HttpStatus httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
