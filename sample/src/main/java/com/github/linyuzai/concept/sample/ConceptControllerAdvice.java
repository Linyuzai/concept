package com.github.linyuzai.concept.sample;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ConceptControllerAdvice {

    @ExceptionHandler(Throwable.class)
    public Object handle(Throwable e) {
        System.out.println("ExceptionHandler");
        e.printStackTrace();
        return e;
    }
}
