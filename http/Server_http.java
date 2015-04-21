package http;

import java.net.*;
import java.io.*;


public class Server_http {

  public static void main(String[] args) throws IOException {
	  ServerSocket serverSock = new ServerSocket(5051);
	  Socket sock = serverSock.accept();

	  InputStream sis = sock.getInputStream();
	  BufferedReader br = new BufferedReader(new InputStreamReader(sis));
	  String request = br.readLine(); // Now you get GET index.html HTTP/1.1`
	  String[] requestParam = request.split(" ");
	  String path = requestParam[1].substring(1);
	  
	  
	  
	  
	  
	  System.out.println(path);
//	  PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
//	  BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	  DataOutputStream out = new DataOutputStream(sock.getOutputStream());
	  
	  File file = new File(path);
	  if (!file.exists()) {
	       out.writeBytes("HTTP/1.1 404 Not Found\r\n"); // the file does not exists
	       out.writeBytes("Content-Type: text/html\r\n\r\n");
	       out.writeBytes("<html><body><H1>404 Not Found</H1></body></html>");
	  }
	  FileReader fr = new FileReader(file);
	  BufferedReader bfr = new BufferedReader(fr);
	  String line;
	  out.writeBytes("HTTP/1.1 200 OK\r\n");
      out.writeBytes("Content-Type: text/html\r\n\r\n");
      
	  while ((line = bfr.readLine()) != null) {
	      out.writeBytes(line);
	      System.out.println(line);
	  }
	 
	  //out.writeBytes("<html><head></head><body><h1>Hello</h1></body></html>");

	  bfr.close();
	  br.close();
	  //out.close();
  }

}
