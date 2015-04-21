package http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class final_httpserver {

	public static void main(String args[]) throws Exception {
		System.out.println("The server is running.");
		int port = 7568;// Integer.parseInt(args[0]);
		int clientNumber = 0;
		
		ServerSocket listener = new ServerSocket(port);

		try {
			while (true) {
				new service(listener.accept(), clientNumber++).start();
			}
		} finally {
			listener.close();
		}
	}

	private static class service extends Thread {
		private Socket socket;
		private int clientNumber;

		public service(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNumber = clientNumber;
			System.out.println("New connection with client# " + clientNumber
					+ " at " + socket);
		}

		public void run() {
			try {
				InputStream sis = socket.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						sis));
				// Now you get GET or PUT index.html HTTP/1.1
				String request = br.readLine(); 
				String[] requestParam = request.split(" ");
				String path = requestParam[1].substring(1);
				System.out.println("Type of Request: " + request);
				//System.out.println("File Requested - " + path);
				DataOutputStream out = new DataOutputStream(
						socket.getOutputStream());

				if (request.contains("GET")) {
					
					path = System.getProperty("user.dir")+"/serverfiles/"+path;
					//System.out.println(path);
					File file = new File(path);
					if (!file.exists()) {
						// the file does not exists
						out.writeBytes("HTTP/1.1 404 Not Found\r\n"); 
						out.flush();
					}
					FileReader fr = new FileReader(file);
					BufferedReader bfr = new BufferedReader(fr);
					String line;
					out.writeBytes("HTTP/1.1 200 OK\r\n");
					out.writeBytes("Content-Type: text/html\r\n\r\n");
					out.flush();
					while ((line = bfr.readLine()) != null) {
						out.writeBytes(line + "\n");
						out.flush();
					}
					System.out.println("File Sent!");
					bfr.close();
					br.close();
				}

				else if (request.contains("PUT")) {

					try {
						out.writeBytes("HTTP/1.1 200 OK File Created\r\n");
						out.flush();
						
						path = System.getProperty("user.dir")+"/serverfiles/"+path;
							
					//	System.out.println(path);

						request = br.readLine();
						System.out.println(request);

//						String filename = "src/http/" + path;
						File file = new File(path);
					//	System.out.println(path);
						String line;
						
						BufferedWriter bufferedWriter = new BufferedWriter(
								new FileWriter(file));

						while ((line = br.readLine()) != null) {
							bufferedWriter.write(line);
							bufferedWriter.write("\r\n");
							bufferedWriter.flush();
					//		System.out.println(line);
						}
						bufferedWriter.flush();
						bufferedWriter.close();
						br.close();
						System.out.println("File Copied at "+path);
					} catch (IOException e) {
						System.err.println("No file received from server: "
								+ e.getMessage());
						br.close();
					}
				} else {
					System.out.println("Wrong Action...use either GET or PUT");
				}

			} catch (IOException e) {
				System.out.println("Error handling client# " + clientNumber
						+ ": " + e.getMessage());
			} finally {
				try {
					System.out.println("Closing Socket...");
					socket.close();
				} catch (IOException e) {
					System.out.println("Couldn't close a socket:"+e.getMessage());
				}
				System.out.println("Connection with client# " + clientNumber
						+ " closed");
				System.out.println();
			}
		}

	}
}