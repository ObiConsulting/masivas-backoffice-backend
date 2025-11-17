package com.novatronic.masivas.backoffice.log;

import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, METHOD})
@InterceptorBinding
public @interface Performance {

}
