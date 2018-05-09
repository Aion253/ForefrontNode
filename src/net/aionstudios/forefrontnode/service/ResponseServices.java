package net.aionstudios.forefrontnode.service;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;

public class ResponseServices {
	
	public static boolean generateHTTPResponse(HttpExchange he, String response) {
		try {
			he.sendResponseHeaders(200, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public static void clearJsonObject(JSONObject j) {
		while(j.length()>0) {
			j.remove((String) j.keys().next());
		}
	}
	
	public static JSONObject getLinkedJsonObject() {
		JSONObject j = new JSONObject();
		Field map;
		try {
			map = j.getClass().getDeclaredField("map");
			map.setAccessible(true);
			map.set(j, new LinkedHashMap<>());
			map.setAccessible(false);
		} catch (NoSuchFieldException e) {
			System.err.println("JSONObject re-link failed!");
			e.printStackTrace();
		} catch (SecurityException e) {
			System.err.println("JSONObject re-link failed!");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.err.println("JSONObject re-link failed!");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println("JSONObject re-link failed!");
			e.printStackTrace();
		}
		return j;
	}

}
