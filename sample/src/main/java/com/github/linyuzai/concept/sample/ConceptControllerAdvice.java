package com.github.linyuzai.concept.sample;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@CommonsLog
@RestControllerAdvice
public class ConceptControllerAdvice {

    @ExceptionHandler(Throwable.class)
    public Object handle(Throwable e) {
        log.error("ExceptionHandler", e);
        return null;
    }
}
