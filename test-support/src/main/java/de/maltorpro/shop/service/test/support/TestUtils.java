package de.maltorpro.shop.service.test.support;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class TestUtils {

	private TestUtils() {
		// not for creation.
	}

	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
