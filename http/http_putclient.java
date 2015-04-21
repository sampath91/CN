package http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class http_putclient {

	public static void main(String[] args) {
		try {
			String path  = "localhost";
			Socket s = new Socket(path,5050);
			String file = "inex.html";
		//	DataOutputStream data_out = new DataOutputStream(s.getOutputStream());
			 
			PrintWriter pw = new PrintWriter(s.getOutputStream());
			pw.print("PUT /inex.html HTTP/1.1\r\n");
			pw.print("Accept: text/plain, text/html, text/*\r\n\r\n");
			
			
			try{
		        FileReader fr = new FileReader(file);
					  BufferedReader bfr = new BufferedReader(fr);
					  String line;
				      
					  while ((line = bfr.readLine()) != null) {
						  pw.write(line);
					      System.out.println(line);
					  }
					  pw.flush();
					
					//Receive OK 201
						  BufferedReader br = new BufferedReader(new InputStreamReader(
								s.getInputStream()));
							System.out.println(br.readLine());
							br.close();
		            
		        }catch (IOException e) {
		            System.err.println(e.getMessage());
		            pw.close();
		        }
			
		} catch (UnknownHostException e) {
			System.err.println("Host unknown. Cannot establish connection");
		} catch (IOException e) {
			System.err.println("Cannot establish connection." + e.getMessage());
		}
	}
}