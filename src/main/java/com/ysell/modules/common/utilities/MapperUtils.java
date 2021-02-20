package com.ysell.modules.common.utilities;

import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class MapperUtils {

	private static ModelMapper mapper = new ModelMapper();


	public static <TSource, TDestination> TDestination map(TSource source, Class<TDestination> targetClass) {
		return mapper.map(source, targetClass);
	}


	public static <TSource, TDestination> TDestination allArgsMap(TSource source, Class<TDestination> targetClass) {
		return mapper.map(source, targetClass);
	}


	public static <TSource, TDestination> Stream<TDestination> toStream(Collection<TSource> source, Class<TDestination> targetClass) {
		return toStream(source, targetClass, (S, D) -> D);
	}


	public static <TSource, TDestination> Stream<TDestination> toStream(Collection<TSource> source, Class<TDestination> targetClass,
																		BiFunction<TSource, TDestination, TDestination> afterEachMap) {
		return source
				.stream()
				.map(element -> {
					TDestination value = mapper.map(element, targetClass);
					value = afterEachMap.apply(element, value);
					return value;
				});
	}
}
