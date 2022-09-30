package com.innrate.common.mapper.model.config;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.InheritingConfiguration;
import org.modelmapper.internal.MappingEngineImpl;
import org.modelmapper.internal.util.Assert;
import org.modelmapper.internal.util.Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


@Getter
public class InnrateModelMapper extends ModelMapper {

    private final MappingEngineImpl engine;

    public InnrateModelMapper() {
        super();
        engine = new MappingEngineImpl((InheritingConfiguration) getConfiguration());
    }

    public <D> D map(Object src, D dst, Type dstType) {
        Assert.notNull(src, "source");
        Assert.notNull(dst, "destination");
        Assert.notNull(dstType, "destinationType");

        return mapInternalOverride(src, dst, dstType, null);
    }

    public <D> D map(Object src, D dst, Class<?> dstElementClass) {
        Assert.notNull(src, "source");
        Assert.notNull(dst, "destination");
        Assert.notNull(dstElementClass, "destinationClass");

        return mapInternalOverride(src, dst, createType(dst.getClass(), dstElementClass), null);
    }

    private Type createType(Class<?> dstClass, Class<?> elementClass) {
        return new ParameterizedTypeImpl(dstClass, elementClass);
    }

    private <D> D mapInternalOverride(Object source, D destination, Type destinationType, String typeMapName) {
        return getEngine().map(
                source,
                Types.deProxy(source.getClass()),
                destination,
                TypeToken.of(destinationType),
                typeMapName);
    }

    protected static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class<?> collectionClass;
        private final Class<?> elementClass;

        public ParameterizedTypeImpl(Class<?> collectionClass, Class<?> elementClass) {
            this.collectionClass = collectionClass;
            this.elementClass = elementClass;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{elementClass};
        }

        @Override
        public Type getRawType() {
            return collectionClass;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
