package com.innrate.common.mapper.model.converter;

import org.modelmapper.internal.util.Iterables;
import org.modelmapper.internal.util.Types;
import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.util.Iterator;

/**
 * Default converter always creates a new array with length = max(source.length, destination.length).
 * Expected a new array with length=source.length.
 * Looks like a bug.
 */
public class InnrateArrayConverter implements ConditionalConverter<Object, Object> {
    @Override
    public MatchResult match(Class<?> sourceType, Class<?> destinationType) {
        return Iterables.isIterable(sourceType) && destinationType.isArray() ? MatchResult.FULL
                : MatchResult.NONE;
    }

    @Override
    public Object convert(MappingContext<Object, Object> context) {
        Object src = context.getSource();
        if (src == null) {
            return null;
        }

        boolean dstProvided = context.getDestination() != null;
        Object dst = createDst(context);

        Class<?> elementType = getElementType(context);
        int index = 0;

        for (Iterator<Object> iterator = Iterables.iterator(src); iterator.hasNext(); index++) {
            Object srcElement = iterator.next();

            if (srcElement == null) {
                Array.set(dst, index, null);
                continue;
            }

            Object dstElement = dstProvided
                    ? Iterables.getElement(dst, index)
                    : null;

            MappingContext<?, ?> elementContext = dstElement == null
                    ? context.create(srcElement, elementType)
                    : context.create(srcElement, dstElement);

            dstElement = context.getMappingEngine().map(elementContext);

            Array.set(dst, index, dstElement);
        }

        return dst;
    }

    private Object createDst(MappingContext<Object, Object> context) {
        int newLength = Iterables.getLength(context.getSource());

        Class<?> dstType = context.getDestinationType();
        Class<?> arrType = dstType.isArray() ? dstType.getComponentType() : dstType;

        Object newArr = Array.newInstance(arrType, newLength);

        Object originDst = context.getDestination();

        if (originDst == null) {
            return newArr;
        }

        int dstLength = Iterables.getLength(originDst);
        //noinspection SuspiciousSystemArraycopy
        System.arraycopy(originDst, 0, newArr, 0, Math.min(dstLength, newLength));

        return newArr;
    }

    private Class<?> getElementType(MappingContext<Object, Object> context) {
        if (context.getGenericDestinationType() instanceof GenericArrayType) {
            return Types.rawTypeFor(context.getGenericDestinationType()).getComponentType();
        }

        return context.getDestinationType().getComponentType();
    }
}
