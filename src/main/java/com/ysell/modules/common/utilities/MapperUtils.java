package com.ysell.modules.common.utilities;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;

public class MapperUtils {
	
	public static <TSource, TDestination> Stream<TDestination> toStream(ModelMapper modelMapper, Collection<TSource> source, Class<TDestination> targetClass) {
		return toStream(modelMapper, source, targetClass, (S, D) -> D);
	}
	
	public static <TSource, TDestination> Stream<TDestination> toStream(ModelMapper modelMapper, Collection<TSource> source, Class<TDestination> targetClass, 
			BiFunction<TSource, TDestination,  TDestination> afterEachMap) {
	    return source
	      .stream()
	      .map(element -> {
	    	  TDestination value = modelMapper.map(element, targetClass);
	    	  value = afterEachMap.apply(element, value);
	    	  return value;
	      });
	}
}
