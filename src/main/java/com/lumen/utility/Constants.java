package com.lumen.utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Constants {

	public static final Map<String, String> CATEGORY_MAPPING = createMap();

	public static Map<String, String> createMap() {
		Map<String, String> result = new HashMap<>();
		result.put("Electronics", "Electronics");
		result.put("Grocery", "grocery");
		result.put("Fashion", "Fashion");
		return Collections.unmodifiableMap(result);
	}
}
