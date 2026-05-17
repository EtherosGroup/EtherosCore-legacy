package com.skilfully.etheros.utils.di.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 从 application.properties 中注入配置值
 *
 * @author Etheros Group
 * @since 1.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Value {
    /**
     * 配置键，如 "server.id"
     */
    String value();

    /**
     * 配置不存在时的默认值，默认为空字符串
     */
    String defaultValue() default "";
}
