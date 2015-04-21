package tcpudp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;

public class servertcp {
	public static void main(String[] args) throws IOException {
		  ServerSocket serverSock = new ServerSocket(8777);
		  Socket sock = serverSock.accept();
		  
		  String padding = "CCCCCCCCCC";
		  char[] buffer = new char[200];
		  
		  for(int i=0;i<0;i++){
			// padding = padding + "CCCCCCCCCC";
		  }
		  System.out.println(padding.length());
		  int seq,rep;
		  String msg=null;
		  InputStream sis = null;
		  long starttime;
		  long [][] ETE = new long[5][1000];
		  long []ETEmax=new long[5];
		  long []ETEavg=new long[5];
		  
		  
//		  DataOutputStream out = new DataOutputStream(sock.getOutputStream());
		  PrintWriter out = new PrintWriter(sock.getOutputStream(),true);
          
		  BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		
		  
		  
		  for(rep = 0;rep < 5;rep++){
			  
		  for(seq = 0;seq < 1000;seq++){
			  msg = seq+1+padding;
//			  starttime = System.nanoTime();
			  starttime = System.currentTimeMillis();
			  
			  //System.out.println(starttime);
			 // System.out.println(msg);
			  out.println(msg);
			  out.flush();
			  msg = br.readLine();
			  ETE[rep][seq]=(System.currentTimeMillis() - starttime)/2;
			//  System.out.println("Rep= "+rep+", ETE (in nano sec) = "+ETE[rep][seq]);
		  }
		  int flag = 0;
		  System.out.println("Length of array ETE "+rep+"= "+ETE[rep].length);
		  ETEmax[rep] = ETE[rep][0];
		  ETEavg[rep] = ETE[rep][0];
		  for ( int i = 1; i < ETE[rep].length; i++) {
		      if ( ETE[rep][i] > ETEmax[rep]) {
		        ETEmax[rep] = ETE[rep][i];
		        flag = i+1;
		      }
		      ETEavg[rep] = ETEavg[rep] + ETE[rep][i];
		  }
		  ETEavg[rep] = ETEavg[rep]/1000;
		  System.out.println("Sequence Number for max. ETE = "+flag);
		  System.out.println("Maximum ETE = "+ETEmax[rep]);
		  System.out.println("Average ETE = "+ETEavg[rep]);
		  System.out.println();
		  }
		  br.close();
		  out.close();
		  
		  
	  }

}
