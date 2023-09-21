package com.shankar.reddit.advice;

import com.shankar.reddit.exception.SpringRedditException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SpringRedditException.class)
    public Map<String,String> handleSpringRedditException(SpringRedditException e){
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("error",e.getMessage()+" "+e.getClass().getName());
        return  errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public Map<String,String> handleUserNotFoundException(UsernameNotFoundException e){
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("error",e.getMessage()+" "+e.getClass().getName());
        return  errorMap;
    }
}
