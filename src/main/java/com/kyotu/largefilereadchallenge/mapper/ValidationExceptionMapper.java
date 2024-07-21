package com.kyotu.largefilereadchallenge.mapper;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;
import java.util.Objects;

public class ValidationExceptionMapper {

    public static List<String> toMessage(HandlerMethodValidationException exception) {
        return exception.getAllValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream())
                .map(MessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull).toList();
    }

}
