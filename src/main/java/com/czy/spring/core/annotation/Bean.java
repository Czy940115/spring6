package com.czy.spring.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: Bean
 * Package: com.czy.spring.core.annotation
 * Description:
 *  通过注解的形式加载bean与实现依赖注入
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Target(ElementType.TYPE)// 注解作用范围
@Retention(RetentionPolicy.RUNTIME)//注解的生命周期
public @interface Bean {
}
