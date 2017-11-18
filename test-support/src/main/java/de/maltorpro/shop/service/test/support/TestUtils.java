package de.maltorpro.shop.service.test.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class TestUtils {

	private TestUtils() {
		// not for creation.
	}

	public static String asJsonString(final Object obj) throws JsonProcessingException {
		
			final ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(obj);
	}
}
