package com.pivotalinitiative.solutions.cloudfoundry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient {
	private int serverSocket;
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private String host;

	public EchoClient(int port, String host) {
		this.serverSocket = port;
		this.host = host;
	}

	public void destroyConnection() throws Exception {
		//socket.close();
	}

	public String sendMessage(String message) throws Exception {
		String messageToReturn = "";
		try {
			socket = new Socket(this.host, this.serverSocket);
			this.writer = new PrintWriter(socket.getOutputStream(), true);
			this.reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			writer.println(message);
			messageToReturn += reader.readLine();
		} catch (Exception e) {
		        return "error = " + e.getMessage() + " host = " + this.host + " port = " + this.serverSocket;
		} finally {
			//writer.close();
			//reader.close();
			//destroyConnection();
		}
		return messageToReturn;
	}
}
