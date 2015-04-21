package tcpudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class udpserver {

	public static void main(String[] args) throws IOException {
		int port = 8777;
		DatagramSocket serverSocket = new DatagramSocket(port);
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];

		String host = "localhost";
		
		  for(int rep = 0;rep < 5;rep++){
		 for(int seq = 0;seq < 1000;seq++){
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				
		serverSocket.receive(receivePacket);

		String sentence = new String( receivePacket.getData());

		System.out.println("RECEIVED: " + sentence);

		InetAddress IPAddress = receivePacket.getAddress();

		port = receivePacket.getPort();

		//String capitalizedSentence = sentence.toUpperCase();

		sendData = sentence.getBytes();

		DatagramPacket sendPacket =

				new DatagramPacket(sendData, sendData.length, IPAddress, port);

		serverSocket.send(sendPacket);
	}
		  }
		serverSocket.close();
		
	}
}

