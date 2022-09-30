package com.innrate.common.mapper.model.converter;

import org.modelmapper.internal.util.Iterables;
import org.modelmapper.internal.util.MappingContextHelper;
import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Default converter always creates a new collection instance for destination.
 * Looks like copy-paste bug.
 *
 * Based on {@link org.modelmapper.internal.converter.MergingCollectionConverter}
 */
public class InnrateCollectionConverter<S, D extends Collection<?>> implements ConditionalConverter<S, D> {

    @Override
    public MatchResult match(Class<?> srcType, Class<?> dstType) {
        return Iterables.isIterable(srcType) && Collection.class.isAssignableFrom(dstType)
                ? MatchResult.FULL
                : MatchResult.NONE;
    }

    @Override
    public D convert(MappingContext<S, D> context) {
        Object src = context.getSource();

        if (src == null) {
            return null;
        }

        D dst = context.getDestination();

        if (dst == null) {
            dst = createCollection(context);
        }

        Class<?> elementType = MappingContextHelper.resolveDestinationGenericType(context);
        int index = 0;

        List<Object> dstTemp = new ArrayList<>();

        for (Iterator<Object> iterator = Iterables.iterator(src); iterator.hasNext(); index++) {
            Object srcElement = iterator.next();
            Object dstElement = Iterables.getElement(dst, index);

            if (srcElement == null) {
                /*
                TODO to skip src null elements or not?
                if (dstElement == null) {
                    dstTemp.add(null);
                }
                */
                continue;
            }
            MappingContext<?, ?> elementContext =
                    dstElement == null
                            ? context.create(srcElement, elementType)
                            : context.create(srcElement, dstElement);

            dstElement = context.getMappingEngine().map(elementContext);
            dstTemp.add(dstElement);
        }

        dst.clear();
        //ModelMapper works with element as Object, so there is no choice but to use unchecked and raw type casting
        //noinspection unchecked,rawtypes
        dst.addAll((List) dstTemp);

        return dst;
    }

    @SuppressWarnings("unchecked")
    private D createCollection(MappingContext<?, ? extends Collection<?>> context) {
        return (D) MappingContextHelper.createCollection((MappingContext<Object, Collection<Object>>) context);
    }
}
