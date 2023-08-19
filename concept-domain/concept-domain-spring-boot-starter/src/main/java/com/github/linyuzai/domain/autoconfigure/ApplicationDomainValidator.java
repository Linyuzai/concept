package com.github.linyuzai.domain.autoconfigure;

import com.github.linyuzai.domain.core.DomainValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import java.util.Objects;

/**
 * 基于 Spring 的校验器
 */
@Getter
@RequiredArgsConstructor
public class ApplicationDomainValidator implements DomainValidator {

    /**
     * org.springframework.validation.Validator
     */
    private final Validator validator;

    @Override
    public void validate(Object target) {
        BindingResult bindingResult = createBindingResult(target);
        validator.validate(target, bindingResult);
        onBindingResult(target, bindingResult);
    }

    /**
     * 创建一个绑定结果容器
     */
    protected BindingResult createBindingResult(Object target) {
        return new DirectFieldBindingResult(target, target.getClass().getSimpleName());
    }

    /**
     * 处理绑定结果
     */
    protected void onBindingResult(Object target, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            FieldError error = Objects.requireNonNull(bindingResult.getFieldError());
            String s = target.getClass().getName() + "#" + error.getField();
            throw new IllegalArgumentException(s + ", " + error.getDefaultMessage());
        }
    }
}
