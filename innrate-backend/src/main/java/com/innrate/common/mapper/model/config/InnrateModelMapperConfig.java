package com.innrate.common.mapper.model.config;

import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.collection.internal.PersistentList;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.internal.converter.MergingCollectionConverter;
import org.modelmapper.spi.ConditionalConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.innrate.common.mapper.model.converter.InnrateArrayConverter;
import com.innrate.common.mapper.model.converter.InnrateCollectionConverter;
import com.innrate.common.mapper.model.converter.InstantConverter;
import com.innrate.common.mapper.model.converter.LocalDateTimeToInstantConverter;

import javax.annotation.Nonnull;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

@Configuration()
public class InnrateModelMapperConfig {

    final Clock clock;

    @Autowired
    public InnrateModelMapperConfig(Clock clock) {
        this.clock = clock;
    }

    @Bean
    @Nonnull
    public InnrateModelMapper getModelMapper() {
        InnrateModelMapper mapper = new InnrateModelMapper();
        //https://stackoverflow.com/questions/36215693/modelmapper-get-confuse-on-different-properties; проблема на 528 строке
        org.modelmapper.config.Configuration configuration = mapper.getConfiguration();
        configuration
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        List<ConditionalConverter<?, ?>> converters = configuration.getConverters();
        converters.add(new InstantConverter(clock));
        converters.add(new LocalDateTimeToInstantConverter(clock));

        updateDefaultConverters(mapper);
        configCollectionsWithDst(mapper);

        return mapper;
    }

    /**
     * There is difference in handling between
     * <p>
     * {@code
     * ArrayList dst = new ArrayList<>();
     * ArrayList dst = mapper.map(src, ArrayList.class);
     * }
     * <p>
     * and
     * <p>
     * {@code
     * ArrayList dst = new ArrayList<>();
     * mapper.map(src, dst);
     * }
     * <p>
     * It's necessary explicit to configure collection mapping to fix this.
     *
     * @param mapper the mapper
     */
    private void configCollectionsWithDst(ModelMapper mapper) {
        mapper.createTypeMap(ArrayList.class, ArrayList.class).setConverter(new InnrateCollectionConverter<>());
        mapper.createTypeMap(ArrayList.class, PersistentBag.class).setConverter(new InnrateCollectionConverter<>());
        mapper.createTypeMap(ArrayList.class, PersistentList.class).setConverter(new InnrateCollectionConverter<>());
    }

    /**
     * Original MergingCollectionConverter returns a new Collection for any case. Looks like a bug.
     * {@link InnrateCollectionConverter} returns the same destination collection.
     *
     * @param mapper the mapper
     */
    private void updateDefaultConverters(ModelMapper mapper) {
        replaceConverter(mapper.getConfiguration().getConverters(), MergingCollectionConverter.class.getName(), new InnrateCollectionConverter<>());
        replaceConverter(mapper.getConfiguration().getConverters(), "org.modelmapper.internal.converter.ArrayConverter", new InnrateArrayConverter());
//        mapper.getConfiguration().getConverters().add(new InstantConverter(clock));
    }

    public void replaceConverter(List<ConditionalConverter<?, ?>> converters,
                                 String converterClassName,
                                 ConditionalConverter<?, ?> converter) {

        ConditionalConverter<?, ?> matchConverter = getConverterByType(converters, converterClassName);

        if (matchConverter != null) {
            int idx = converters.indexOf(matchConverter);
            if (idx != -1) {
                converters.remove(matchConverter);
                converters.add(idx, converter);
            }
        }
    }

    private ConditionalConverter<?, ?> getConverterByType(List<ConditionalConverter<?, ?>> converters,
                                                          String converterClassName) {
        for (ConditionalConverter<?, ?> converter : converters) {
            if (converterClassName.equals(converter.getClass().getName()))
                return converter;
        }
        return null;
    }
}
