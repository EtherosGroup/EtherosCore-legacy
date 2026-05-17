package com.skilfully.etheros.utils.di.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记需要自动注入的字段，容器会在refresh时自动赋值
 *
 * @author Etheros Group
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface Autowired {
    /**
     * 是否必须注入，默认true。若为false且未找到Bean则跳过不抛异常
     */
    boolean required() default true;
}
