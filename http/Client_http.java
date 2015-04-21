package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client_http {

	public static void main(String[] args) {
		try {
			String path  = "google.com";
			Socket s = new Socket(path,80);
			PrintWriter pw = new PrintWriter(s.getOutputStream());
//			pw.println("GET / HTTP/1.1");
//			pw.println("Host: www.google.com");
//			
//			pw.print(  "GET " + path + " HTTP/1.1\r\n" + 
//			                       "Host: " + path + "\r\n" + 
//			                       "Connection: close\r\n\r\n"); 
			pw.print("GET /index.html HTTP/1.1\r\n");
	         pw.print("Accept: text/plain, text/html, text/*\r\n");
	         pw.print("\r\n");
			pw.flush( ); 
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			String t;
			String gt=br.readLine();
			if(gt.contains("200")){
				System.out.println(gt);
				while ((t = br.readLine()) != null)
					System.out.println(t);
				br.close();
			}else if(gt.contains("404")){
				System.out.println(gt);
			}
			
		} catch (UnknownHostException e) {
			System.err.println("Host unknown. Cannot establish connection");
		} catch (IOException e) {
			System.err.println("Cannot establish connection." + e.getMessage());
		}
	}
}