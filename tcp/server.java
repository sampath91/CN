package tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server program which accepts requests from clients to capitalize strings.
 * When clients connect, a new thread is started to handle an interactive
 * diaSystem.out.println in which the client sends in a string and the server
 * thread sends back the capitalized version of the string.
 *
 * The program is runs in an infinite loop, so shutdown in platform dependent.
 * If you ran it from a console window with the "java" interpreter, Ctrl+C
 * generally will shut it down.
 */

public class server {

	/**
	 * Application method to run the server runs in an infinite loop listening
	 * on port. When a connection is requested, it spawns a new thread to do the
	 * servicing and immediately returns to listening. The server keeps a unique
	 * client number for each client that connects just to show interesting
	 * System.out.printlnging messages. It is certainly not necessary to do
	 * this.
	 */
	public static void main(String args[]) throws Exception {
		System.out.println("The server is running.");
		int port = Integer.parseInt(args[0]);
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

	/**
	 * A private thread to handle capitalization requests on a particular
	 * socket. The client terminates the diaSystem.out.printlnue by sending a
	 * single line containing only a period.
	 */
	private static class service extends Thread {
		private Socket socket;
		private int clientNumber;

		public service(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNumber = clientNumber;
			System.out.println("New connection with client# " + clientNumber
					+ " at " + socket);
		}

		/**
		 * Services this thread's client by first sending the client a welcome
		 * message then repeatedly reading strings and sending back the
		 * capitalized version of the string.
		 */
		public void run() {
			try {

				// Decorate the streams so we can send characters
				// and not just bytes. Ensure output is flushed
				// after every newline.
				BufferedReader in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(),
						true);
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				OutputStream os = null;

				// Send a welcome message to the client.
				// out.println("Hello, you are client #" + clientNumber + ".");
				// out.println("Enter a line with only a period to quit\n");
				String action = in.readLine();

				System.out.println("Type of Request: " + action);

				if (action.equalsIgnoreCase("get")) {
					String filelocation;
					filelocation = in.readLine();
					File myFile = new File(filelocation);
					if (myFile.exists()) {
						out.println("200");
						byte[] mybytearray = new byte[(int) myFile.length()];
						fis = new FileInputStream(myFile);
						bis = new BufferedInputStream(fis);
						bis.read(mybytearray, 0, mybytearray.length);
						os = socket.getOutputStream();

						System.out.println("Sending " + filelocation + "("
								+ mybytearray.length + " bytes)");
						os.write(mybytearray, 0, mybytearray.length);
						os.flush();
						System.out.println("File Sent!!!");

					} else {
						out.println("404");
					}

				} else if (action.equalsIgnoreCase("put")) {
System.out.println("PUTTTTT");
					int bytesRead;
					int current = 0;
					FileOutputStream fos = null;
					BufferedOutputStream bos = null;

					String filereceived = "/home/sam/workspace/CN/src/tcp/Clientfile";
					// you may change this, I give a
					// different name because i don't want to
					// overwrite the one used by server...

					int FILE_SIZE = 6022386; // file size temporary hard coded

					byte[] mybytearray = new byte[FILE_SIZE];
					System.out.println("about to send");
					InputStream is = socket.getInputStream();
					fos = new FileOutputStream(filereceived);
					bos = new BufferedOutputStream(fos);
					bytesRead = is.read(mybytearray, 0, mybytearray.length);
					current = bytesRead;
					System.out.println(current);
					do {
						bytesRead = is.read(mybytearray, current,
								(mybytearray.length - current));
						if (bytesRead >= 0)
							current += bytesRead;
					} while (bytesRead > -1);

					bos.write(mybytearray, 0, current);
					bos.flush();
//					bos.close();
					System.out.println("File " + filereceived + " downloaded ("
							+ current + " bytes read)");
					out.println("200");
//					File clientfile = new File(filereceived);		
//					if(clientfile.exists()){
//
//						out.println("200");
//
//					}else{
//						out.println("404");
//					}

				} else {
					System.out.println("Wrong Action...use either GET or PUT");
				}

			
			} catch (IOException e) {
				System.out.println("Error handling client# " + clientNumber
						+ ": " + e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.out
							.println("Couldn't close a socket, what's going on?");
				}
				System.out.println("Connection with client# " + clientNumber
						+ " closed");
				System.out.println();
			}
		}

	}
}