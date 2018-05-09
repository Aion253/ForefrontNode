package net.aionstudios.forefrontnode;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.JSONException;

import net.aionstudios.aionlog.AnsiOut;
import net.aionstudios.aionlog.Logger;
import net.aionstudios.aionlog.StandardOverride;
import net.aionstudios.forefrontnode.config.Config;
import net.aionstudios.forefrontnode.server.NodeServer;
import net.aionstudios.forefrontnode.util.SecurityUtils;

public class ForefrontNode {
	
	public static Config rootConfig;
	
	public static void main(String[] args) throws JSONException, IOException {
		System.setProperty("java.net.preferIPv4Stack" , "true");
		System.out.println("Starting Forefront Node...");
		setupDefaults();
		if(((boolean) rootConfig.getConfig().get("log_enabled"))) {
			File f = new File("./logs/");
			f.mkdirs();
			Logger.setup();
		}
		SecurityUtils.securityKey = (String) rootConfig.getConfig().get("security_key");
		SecurityUtils.securitySecret = (String) rootConfig.getConfig().get("security_secret");
		String serverName = (String) rootConfig.getConfig().get("server_name");
		SecurityUtils.serverName = serverName;
		AnsiOut.initialize();
		AnsiOut.setStreamPrefix("Forefront");
		StandardOverride.enableOverride();
		
		NodeServer nodeServer = new NodeServer((int) rootConfig.getConfig().get("server_port"));
	}
	
	private static void setupDefaults() {
		readConfigs();
	}
	
	public static boolean readConfigs() {
		try {
			File rtcf = new File(NodeInfo.FFT_ROOT_CONFIG);
			rootConfig = new Config(rtcf);
			if(!rtcf.exists()) {
				rtcf.getParentFile().mkdirs();
				rtcf.createNewFile();
				System.err.println("Fill out the config at "+rtcf.getCanonicalPath()+" and restart the server!");
				rootConfig.getConfig().put("server_name", "");
				rootConfig.getConfig().put("server_port", 26736);
				rootConfig.getConfig().put("log_enabled", true);
				rootConfig.getConfig().put("append_ip_on_name", true);
				rootConfig.getConfig().put("encrypt_connection", true);
				rootConfig.getConfig().put("security_key", SecurityUtils.genToken(32));
				rootConfig.getConfig().put("security_secret", SecurityUtils.genToken(128));
				rootConfig.writeConfig();
				System.exit(0);
			} else {
				rootConfig.setRequiredKeys("server_name", "server_port", "log_enabled", "append_ip_on_name", "encrypt_connection", "security_key", "security_secret");
				if(!rootConfig.readConfig()) {
					System.out.println("Missing or malformed config data! Ensure all variables are set. "+rootConfig.getRequiredKeys());
					System.exit(0);
				}
			}
			return true;
		} catch (IOException e) {
			System.err.println("Encountered an IOException during config file operations!");
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			System.err.println("Encountered an JSONException during config file operations!");
			e.printStackTrace();
			return false;
		}
	}

}
