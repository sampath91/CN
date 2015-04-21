package http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class final_httpclient {

	private String hostname;
	private int port;
	Socket socketClient;

	public final_httpclient(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public void connect() throws UnknownHostException, IOException {
		// System.out.println("Attempting to connect to "+hostname+":"+port);
		socketClient = new Socket(hostname, port);
		System.out.println("TCP Connection Established to " + hostname + ":"
				+ port);
		System.out.println();
	}

	public void getreadResponse(String filename) throws IOException {

		PrintWriter pw = new PrintWriter(socketClient.getOutputStream());
		pw.print("GET /" + filename + " HTTP/1.1\r\n");
		pw.print("Accept: text/plain, text/html, text/*\r\n");
		pw.print("\r\n");
		pw.flush();

		BufferedReader br = new BufferedReader(new InputStreamReader(
				socketClient.getInputStream()));
		String t;
		String gt = br.readLine();
		if (gt.contains("200")) {
			System.out.println(gt);
			while ((t = br.readLine()) != null)
				System.out.println(t);
			br.close();
		} else if (gt.contains("404")) {
			System.out.println(gt);
		}

	}

	public void putsendResponse(String filename) throws IOException {

		PrintWriter pw = new PrintWriter(socketClient.getOutputStream());
		pw.print("PUT /" + filename + " HTTP/1.1\r\n");
		pw.print("Accept: text/plain, text/html, text/*\r\n\r\n");
		
		
		// send a file code
		try {
			filename = System.getProperty("user.dir")+"/clientfiles/"+filename;
			FileReader fr = new FileReader(filename);
			BufferedReader bfr = new BufferedReader(fr);
			String line;

			while ((line = bfr.readLine()) != null) {
				pw.write(line);
				System.out.println(line);
			}
			pw.flush();

			// Receive OK 201
			BufferedReader br = new BufferedReader(new InputStreamReader(
					socketClient.getInputStream()));
			System.out.println(br.readLine());
			br.close();

		} catch (IOException e) {
			System.err.println(e.getMessage());
			pw.close();
		}
	}

	public static void main(String[] args) {

		// String host = args[0];
		// int port = Integer.parseInt(args[1]);
		// String command = args[2];
		// String filename = args[3];

		String host = "localhost";
		int port = 7568;
		String command = "put";
		String filename = "Home.html";

		// Creating a SocketClient object
		final_httpclient client = new final_httpclient(host, port);

		try {
			// trying to establish connection to the server
			client.connect();
			// if successful, send action to server
			// client.getorput(command);

		} catch (UnknownHostException e) {
			System.err.println("Host unknown. Cannot establish connection");
		} catch (IOException e) {
			System.err.println("Cannot establish connection." + e.getMessage());
		}

		if (command.equalsIgnoreCase("get")) {

			try {
				client.getreadResponse(filename);
			} catch (IOException e) {
				System.err.println("Cannot establish connection."
						+ e.getMessage());
			}

		} else if (command.equalsIgnoreCase("put")) {
			try {
				client.putsendResponse(filename);

			} catch (IOException e) {
				System.err.println("Cannot establish connection."
						+ e.getMessage());
			}
		} else {
			System.out.println("Please enter Correct Command");
		}

	}
}
