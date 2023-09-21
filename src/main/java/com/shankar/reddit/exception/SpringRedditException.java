package com.shankar.reddit.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

public class SpringRedditException extends  RuntimeException{


    public SpringRedditException(String msg) {
        super(msg);
    }

    public SpringRedditException(String msg,Exception e){
        super(msg,e);
    }
}
