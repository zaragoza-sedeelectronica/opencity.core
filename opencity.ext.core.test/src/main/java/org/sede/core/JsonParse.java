package org.sede.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.sede.core.utils.Funciones;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonParse {

	public static String getFirstId(MvcResult result) throws UnsupportedEncodingException, IOException {
		JsonNode json = Funciones.asJson(result.getResponse().getContentAsString());
		for (JsonNode reg : json.get("result")) {
			return reg.get("id").asText();
		}
		throw new IOException("No se ha podido obtener ningún identificador");
	}

	
}
