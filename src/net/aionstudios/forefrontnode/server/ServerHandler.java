package net.aionstudios.forefrontnode.server;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import net.aionstudios.forefrontnode.service.ResponseServices;
import net.aionstudios.forefrontnode.util.RequestUtils;
import net.aionstudios.forefrontnode.util.SecurityUtils;

public class ServerHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange he) throws IOException {
		if(he.getRequestURI().toString().length()<2) {
			Map<String, String> postQuery = new HashMap<String, String>();
			if(he.getRequestMethod().equalsIgnoreCase("POST")) {
				postQuery = RequestUtils.resolvePostQuery(he);
				if(postQuery.containsKey("action")) {
					if(postQuery.get("action").equals("login")) {
						if(postQuery.containsKey("key")&&postQuery.containsKey("secret")) {
							if(postQuery.get("key").equals(SecurityUtils.securityKey)&&postQuery.get("secret").equals(SecurityUtils.securitySecret)) {
								String secureToken = SecurityUtils.genToken(256);
								SecurityUtils.tokens.add(secureToken);
								JSONObject j = new JSONObject();
								try {
									j.put("server_name", SecurityUtils.serverName);
									j.put("token", secureToken);
									ResponseServices.generateHTTPResponse(he, j.toString(2));
									return;
								} catch (JSONException e) {
									System.out.println("Encountered a JSONException writing response");
									ResponseServices.generateHTTPResponse(he, "{ \"error\":\"response failed\" }");
									e.printStackTrace();
								}
							}
						}
					} else if (postQuery.get("action").equals("update")) {
						if(postQuery.containsKey("token")) {
							if(SecurityUtils.tokens.contains(postQuery.get("token"))) {
								OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
								double totalMem = osBean.getTotalPhysicalMemorySize();
								double usedMem = osBean.getTotalPhysicalMemorySize()-osBean.getFreePhysicalMemorySize();
								double cpuUsed = osBean.getSystemCpuLoad();
								JSONObject j = new JSONObject();
								try {
									j.put("ram_total", totalMem/1073741824);
									j.put("ram_used", usedMem/1073741824);
									j.put("cpu_load", cpuUsed);
									ResponseServices.generateHTTPResponse(he, j.toString(2));
								} catch (JSONException e) {
									System.out.println("Encountered a JSONException writing response");
									ResponseServices.generateHTTPResponse(he, "{ \"error\":\"response failed\" }");
									e.printStackTrace();
								}
							} else {
								ResponseServices.generateHTTPResponse(he, "{ \"error\":\"token invalid\" }");
							}
						}
					}
				}
			}
		}
		ResponseServices.generateHTTPResponse(he, "{}");
	}

}
