package fit.fancyday.common.exception;

import fit.fancyday.model.common.enums.HttpCodeEnum;


/**
 * Javabean for a custom exception class
 *
 */
public class CustomException extends RuntimeException{
    private HttpCodeEnum httpCodeEnum;

    public CustomException(HttpCodeEnum httpCodeEnum) {
        this.httpCodeEnum = httpCodeEnum;
    }

    public HttpCodeEnum getHttpCodeEnum() {
        return httpCodeEnum;
    }
}
