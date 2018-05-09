package net.aionstudios.forefrontnode.util;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONObject;

public class SecurityUtils {
	
	private static final String tokenChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static SecureRandom rnd = new SecureRandom();
	
	public static String securityKey;
	public static String securitySecret;
	public static List<String> tokens = new ArrayList<String>();
	
	public static String serverName;

	/**
	 * Generates a token of the given length
	 * @param length	The length of token to generate
	 * @return	A token
	 */
	public static String genToken(int length){
	   StringBuilder sb = new StringBuilder(length);
	   for( int i = 0; i < length; i++ ) 
	      sb.append( tokenChars.charAt( rnd.nextInt(tokenChars.length()) ) );
	   return sb.toString();
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
