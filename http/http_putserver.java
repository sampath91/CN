package http;

import java.net.*;
import java.io.*;

public class http_putserver {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSock = new ServerSocket(5050);
		Socket sock = serverSock.accept();

		InputStream sis = sock.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(sis));
		String request = br.readLine(); // Now you get PUT index.html HTTP/1.1
		String[] requestParam = request.split(" ");
		String path = requestParam[1].substring(1);

		DataOutputStream out = new DataOutputStream(sock.getOutputStream());
try{
		out.writeBytes("HTTP/1.1 201 Created\r\n");
		//out.writeBytes("Content-Type: text/html\r\n\r\n");
		out.flush();

		System.out.println(path);

		request = br.readLine();
		System.out.println(request);

		// PrintWriter out = new PrintWriter(sock.getOutputStream());
		String filename = "src/http/" + path;
		File file = new File(filename);
		System.out.println(filename);
		String line;
		

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

		while ((line = br.readLine()) != null) {
			// out.writeBytes(line);
			bufferedWriter.write(line);
			// bufferedWriter.write("\n");
			bufferedWriter.flush();
			System.out.println(line);
		}
		bufferedWriter.flush();
		bufferedWriter.close();
		br.close();
}catch (IOException e) {
    System.err.println("No file received from server: "+e.getMessage());
    br.close();
}
	}

}
