package com.innrate.backend.service.common;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RestController;
import com.innrate.common.spring.controller.InnrateRequestMapping;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation can be used instead of the both {@code RestController} and {@link InnrateRequestMapping} to simplify usage.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@InnrateRequestMapping
public @interface InnrateController {

    @AliasFor(annotation = InnrateRequestMapping.class)
    String pathPrefix() default "${innrate.backend-path-prefix}";

    /**
     * Empty string in default value is required to use empty @InnrateController to add path prefix to method's paths.
     * Path prefix can be add only to some path. If there is no one path specified, the prefix will not be added.
     *
     * @return default path
     */
    @AliasFor(annotation = InnrateRequestMapping.class)
    String[] value() default {""};
}