package com.innrate.common.spring.controller;

import com.google.common.base.Strings;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import com.innrate.common.ReflectUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * The handler to support {@link GpnRequestMapping}
 */
public class InnrateHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    @NonNull
    protected RequestMappingInfo createRequestMappingInfo(@NonNull RequestMapping requestMapping,
                                                          @Nullable RequestCondition<?> customCondition) {

        String[] path = getPath(requestMapping);

        RequestMappingInfo.Builder builder = RequestMappingInfo
                .paths(resolveEmbeddedValuesInPatterns(path))
                .methods(requestMapping.method())
                .params(requestMapping.params())
                .headers(requestMapping.headers())
                .consumes(requestMapping.consumes())
                .produces(requestMapping.produces())
                .mappingName(requestMapping.name());

        if (customCondition != null) {
            builder.customCondition(customCondition);
        }
        return builder.options(getConfig()).build();
    }

    private String[] getPath(RequestMapping requestMapping) {
        String[] path = requestMapping.path();
        String prefix = getPrefix(requestMapping);

        if (Strings.isNullOrEmpty(prefix)) {
            return path;
        }

        return Arrays.stream(path)
                .map(s -> prefix + s)
                .toArray(String[]::new);
    }

    @Nullable
    private String getPrefix(RequestMapping requestMapping) {
        if (!Proxy.isProxyClass(requestMapping.getClass())) {
            return null;
        }

        InvocationHandler h = Proxy.getInvocationHandler(requestMapping);
        MergedAnnotation<?> annotation = ReflectUtil.readField(h, "annotation");
        // Root cannot  be null. If annotation has not root, root=annotation.
        MergedAnnotation<?> root = annotation.getRoot();

        return root.getValue("pathPrefix", String.class).orElse(null);
    }

    /**
     * Gets builder configuration.
     * This getter fixes design mistake in {@link RequestMappingHandlerMapping} with no access to the field in inheritors.
     *
     * @return the configuration
     */
    protected RequestMappingInfo.BuilderConfiguration getConfig() {
        return ReflectUtil.readField(this, "config");
    }
}