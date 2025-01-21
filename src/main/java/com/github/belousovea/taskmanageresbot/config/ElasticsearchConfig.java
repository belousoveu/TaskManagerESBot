package com.github.belousovea.taskmanageresbot.config;

import com.github.belousovea.taskmanageresbot.converter.LocalDateTimeToStringConverter;
import com.github.belousovea.taskmanageresbot.converter.StringToLocalDateTimeConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ElasticsearchConfig extends ElasticsearchConfigurationSupport {

    @Override
    public @NotNull ElasticsearchCustomConversions elasticsearchCustomConversions() {
        List<Converter<?,?>> converters = new ArrayList<>();
        converters.add(new LocalDateTimeToStringConverter());
        converters.add(new StringToLocalDateTimeConverter());
        return new ElasticsearchCustomConversions(converters);
    }
}
