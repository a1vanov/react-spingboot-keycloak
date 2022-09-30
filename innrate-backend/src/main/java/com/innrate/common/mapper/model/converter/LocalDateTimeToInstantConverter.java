package com.innrate.common.mapper.model.converter;

import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

public class LocalDateTimeToInstantConverter implements ConditionalConverter<LocalDateTime, Instant> {

    private final Clock clock;

    public LocalDateTimeToInstantConverter(Clock clock) {
        this.clock = clock;
    }

    @Override
    public MatchResult match(Class<?> sourceType, Class<?> destinationType) {
        return LocalDateTime.class.isAssignableFrom(sourceType) && Instant.class.isAssignableFrom(destinationType)
                ? MatchResult.FULL
                : MatchResult.NONE;
    }

    @Override
    public Instant convert(MappingContext<LocalDateTime, Instant> context) {
        LocalDateTime localDateTime = context.getSource();
        return localDateTime != null ? localDateTime.atZone(clock.getZone()).toInstant() : null;
    }
}
