package es.udc.ws.util.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.ws.util.json.ObjectMapperFactory;

public class ServletUtils {

	public static final int DEFAULT_BUFFER_SIZE = 1024 * 8;

	public static void writeServiceResponse(HttpServletResponse response, int responseCode, JsonNode rootNode,
			Map<String, String> headers) throws IOException {

		writeResponse(response, responseCode, "application/json", headers);

		if (rootNode != null) {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			objectMapper.writer(new DefaultPrettyPrinter()).writeValue(response.getOutputStream(), rootNode);
		}
	}

	public static void writeServiceResponse(HttpServletResponse response, int responseCode, String contentType,
			InputStream content, Map<String, String> headers) throws IOException {

		writeResponse(response, responseCode, contentType, headers);

		if (content != null) {
			copy(content, response.getOutputStream());
		}
	}

	private static void writeResponse(HttpServletResponse response, int responseCode, String contentType,
			Map<String, String> headers) {
		if (headers != null && !headers.isEmpty()) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				response.setHeader(key, value);
			}
		}
		response.setStatus(responseCode);
		if (contentType != null) {
			response.setContentType(contentType);
		}
	}

	public static String normalizePath(String url) {
		if (url != null && url.endsWith("/")) {
			return url.substring(0, url.length() - 1);
		} else {
			return url;
		}
	}

	private static int copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

}
