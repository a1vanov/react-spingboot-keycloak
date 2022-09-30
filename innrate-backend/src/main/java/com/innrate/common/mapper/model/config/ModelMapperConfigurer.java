package com.innrate.common.mapper.model.config;

import org.modelmapper.Condition;
import org.modelmapper.Converter;
import com.innrate.common.database.model.Base;

import java.util.UUID;

public class ModelMapperConfigurer {

    /*
     * Use this converter only for unidirectional link (Dict or Org)
     */
    protected final Converter<UUID, ? extends Base> uuidUnidirectionalConverter = context -> {

        UUID src = context.getSource();

        if (src == null) {
            return null;
        }

        if (context.getDestination() != null && src.equals(context.getDestination().getId())) {
            return context.getDestination();
        }

        Class<?> clazz = context.getDestinationType();
        try {
            Base dst = (Base) clazz.newInstance();
            dst.setId(src);
            return dst;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage());
        }
    };

    protected final Condition<?, ?> isSrcNull = context -> context.getSource() == null;
}
