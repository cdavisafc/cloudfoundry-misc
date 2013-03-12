package com.pivotalinitiative.solutions.cloudfoundry;

public class EchoServiceCreds {
	private String host;
	private int port;

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public EchoServiceCreds(int port, String host) {
		this.host = host;
		this.port = port;
	}
}
