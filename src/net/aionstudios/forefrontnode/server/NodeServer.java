package net.aionstudios.forefrontnode.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class NodeServer {
	
	HttpServer server;
	int serverPort = 26736;
	
	public NodeServer(int port) throws IOException {
		if(port<0||port>65535) {
			System.out.println("No valid server port, using default "+serverPort);
		} else {
			this.serverPort=port;
		}
		server = HttpServer.create(new InetSocketAddress(serverPort), 0);
		server.createContext("/", new ServerHandler());
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		System.out.println("Server started on port " + port);
	}

}
