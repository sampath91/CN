package tcpudp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

//Sender

public class clienttcp {
	public static void main(String[] args) {
		try {
			String path = "localhost";
			Socket s = new Socket(path, 8777);
			int seq;

			BufferedReader br = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			PrintWriter out = new PrintWriter(s.getOutputStream(), true);

		
			for (seq = 0; seq < 1000; seq++) {
				String msg = br.readLine();
				System.out.println(msg);
//				String size = Integer.toString(seq);
//				msg = msg.substring(size.length());
				System.out.println(msg);
				out.println(msg);
				out.flush();
			}
			br.close();
			out.close();

		} catch (UnknownHostException e) {
			System.err.println("Host unknown. Cannot establish connection");
		} catch (IOException e) {
			System.err.println("Cannot establish connection." + e.getMessage());
		}
	}

}
