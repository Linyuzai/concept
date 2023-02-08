package com.github.linyuzai.domain.core.exception;


import com.github.linyuzai.domain.core.lambda.LambdaFunction;

public class DomainRequiredException extends DomainException {

    public <T, R> DomainRequiredException(LambdaFunction<T, R> cf) {
        this(LambdaFunction.getClassName(cf.getSerializedLambda())
                + " " +
                LambdaFunction.getMethodName(cf.getSerializedLambda()).lowercaseFirst());
    }

    public <T, R> DomainRequiredException(String target) {
        super(target + " required");
    }
}
