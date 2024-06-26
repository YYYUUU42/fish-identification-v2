package com.yunqi.fish.exception;


import com.yunqi.fish.common.AppHttpCodeEnum;

public class CustomException extends RuntimeException {

    private int code;

    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public CustomException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMessage());
        this.code = httpCodeEnum.getCode();
        this.message = httpCodeEnum.getMessage();
    }

    public CustomException(int code,String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}
