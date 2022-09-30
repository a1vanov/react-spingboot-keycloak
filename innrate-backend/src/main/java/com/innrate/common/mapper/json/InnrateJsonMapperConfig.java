package com.innrate.common.mapper.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InnrateJsonMapperConfig {

    @Bean
    public ObjectMapper createJsonMapper() {
        return new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                // for java.time package
                .registerModule(new JavaTimeModule())

                //instead of @JsonIgnoreProperties(ignoreUnknown = true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true)

                // serialization - deserialization in "millis" instead of "seconds.nanos"
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
    }
}
