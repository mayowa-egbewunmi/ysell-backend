package com.ysell.modules.common.utilities;

import org.modelmapper.ModelMapper;

public class MapperUtils {

	private static ModelMapper mapper = new ModelMapper();


	public static <TSource, TDestination> TDestination allArgsMap(TSource source, Class<TDestination> targetClass) {
		return mapper.map(source, targetClass);
	}
}
