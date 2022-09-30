package com.innrate.common.spring.controller;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation can be used instead of {@code RequestMapping} and
 * has an additional field {@code pathPrefix} which will be added to each path in the {@link InnrateRequestMapping#path} field.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping
public @interface InnrateRequestMapping {

    /**
     * Defines path prefix for each path in the {@link #path} field.
     *
     * @see InnrateRequestMapping
     */
    String pathPrefix() default "";

    @AliasFor(annotation = RequestMapping.class)
    String name() default "";

    @AliasFor(annotation = RequestMapping.class)
    String[] value() default {""};

    @AliasFor(annotation = RequestMapping.class)
    String[] path() default {""};

    @AliasFor(annotation = RequestMapping.class)
    RequestMethod[] method() default {};

    @AliasFor(annotation = RequestMapping.class)
    String[] params() default {};

    @AliasFor(annotation = RequestMapping.class)
    String[] headers() default {};

    @AliasFor(annotation = RequestMapping.class)
    String[] consumes() default {};

    @AliasFor(annotation = RequestMapping.class)
    String[] produces() default {};
}
