package com.kenda.webflux.restful.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.lang.reflect.Type;
import java.util.List;

public class GenericConverter {

    private GenericConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static <T, E> E mapper(T source, Class<E> typeDestination) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(source, typeDestination);

    }

    public static <T, E> E mapper(T source, E destination) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(source, destination);

        return destination;
    }

    public static <E, T> List<E> mapper(List<T> source, Type destinationType) {

        List<E> model = null;
        if (source != null && destinationType != null) {

            ModelMapper modelMapper = new ModelMapper();

            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            model = modelMapper.map(source, destinationType);
        }

        return model;
    }

}
