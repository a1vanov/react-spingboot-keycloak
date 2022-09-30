package com.innrate.common.mapper.model.converter;

import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class InstantConverter implements ConditionalConverter<String, Instant> {

    private final Clock clock;

    public InstantConverter(Clock clock) {
        this.clock = clock;
    }

    @Override
    public MatchResult match(Class<?> sourceType, Class<?> destinationType) {
        return String.class.isAssignableFrom(sourceType) && Instant.class.isAssignableFrom(destinationType)
                ? MatchResult.FULL
                : MatchResult.NONE;
    }

    @Override
    public Instant convert(MappingContext<String, Instant> context) {
        String str = context.getSource();
        LocalDateTime localDateTime = LocalDateTime.parse(str);
        return ZonedDateTime.of(localDateTime, clock.getZone()).toInstant();
    }
}
