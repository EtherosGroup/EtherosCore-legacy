package com.skilfully.etheros.utils.di.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个类为单例Bean，由容器管理其生命周期
 *
 * @author Etheros Group
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Service {
    /**
     * Bean名称，默认为空（使用类名首字母小写作为名称）
     */
    String value() default "";
}
