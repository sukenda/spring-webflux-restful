package com.kenda.webflux.restful.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.stream.Collectors;

public class GenericConverter {

    private GenericConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static <T, E> E mapper(T source, Class<E> targetClass) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper.map(source, targetClass);

    }

    public static <S, T> List<T> mapperList(List<S> sources, Class<T> targetClass) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return sources
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

}
