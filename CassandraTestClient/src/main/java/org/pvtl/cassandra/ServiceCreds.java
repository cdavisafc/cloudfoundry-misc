package org.pvtl.cassandra;

public class ServiceCreds {
	private String host;
	private int port;
	private String name;

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getName() {
		return name;
	}

	public ServiceCreds(String name, int port, String host) {
		this.host = host;
		this.port = port;
		this.name = name;
	}
}
