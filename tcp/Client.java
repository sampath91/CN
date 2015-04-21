package tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private String hostname;
    private int port;
    Socket socketClient;

    public Client(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    public void connect() throws UnknownHostException, IOException{
        System.out.println("Attempting to connect to "+hostname+":"+port);
        System.out.println();
        socketClient = new Socket(hostname,port);
        System.out.println("TCP Connection Established to "+hostname+":"+port);
    }

    public void getreadResponse(String filename) throws IOException{

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);

        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        
        out.println(filename);
        
        String code = stdIn.readLine();
        
        if(code.equals("200")){
        	System.out.println("200 OK\n");
        	String filereceived = "/home/sam/workspace/CN/src/http/filefromserver";  // you may change this, I give a
                                                                 // different name because i don't want to
                                                                 // overwrite the one used by server...

        	int FILE_SIZE = 6022386; // file size temporary hard coded
                                                    
            byte [] mybytearray  = new byte [FILE_SIZE];
            InputStream is = socketClient.getInputStream();
            fos = new FileOutputStream(filereceived);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;

            do {
               bytesRead =
                  is.read(mybytearray, current, (mybytearray.length-current));
               if(bytesRead >= 0) current += bytesRead;
            } while(bytesRead > -1);

            bos.write(mybytearray, 0 , current);
            bos.flush();
            System.out.println("File " + filereceived
                + " downloaded (" + current + " bytes read)");
          
        	//taking file
        }else if(code.equals("404")){
        	System.out.println("404 Not Found");
        }
    }
    
    public void putsendResponse(String filename) throws IOException{
        
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        
        //send a file code
        File myFile = new File (filename);
        if(myFile.exists()){
        	byte [] mybytearray  = new byte [(int)myFile.length()];
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
            bis.read(mybytearray,0,mybytearray.length);
            os = socketClient.getOutputStream();
            System.out.println("Sending " + filename + "(" + mybytearray.length + " bytes)");
            os.write(mybytearray,0,mybytearray.length);
            os.flush();
            System.out.println("File Sent!!!");
            
            String msg = stdIn.readLine();	//200 OK File Created
            if(msg.equals("200")){
            	System.out.println("200 OK File Created");
            }else if(msg.equals("404")){
            	System.out.println("File Not Sent to Server");
            }
        }else{
        	System.out.println("There is no such file to send!!!");
        }
    }
    
    public void getorput(String getorput) throws IOException{
    	if(getorput.equalsIgnoreCase("get")){
        System.out.println("Submitting HTTP/1.1 GET request to the server");
    	}else{
    		System.out.println("Submitting HTTP/1.1 PUT request to the server");
        	
    	}
    		
        PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
        out.println(getorput);
	}

    public static void main(String [] args){

    	String host = args[0];
		int port = Integer.parseInt(args[1]);
		String command = args[2];
		String filename = args[3];

			        
		//Creating a SocketClient object
        Client client = new Client (host,port);
        
        try {
            //trying to establish connection to the server
            client.connect();
            //if successful, send action to server
            client.getorput(command);
         
        } catch (UnknownHostException e) {
            System.err.println("Host unknown. Cannot establish connection");
        } catch (IOException e) {
            System.err.println("Cannot establish connection."+e.getMessage());
        }
        
        if(command.equalsIgnoreCase("get")){
    
        	try {
            	client.getreadResponse(filename);
			} catch (IOException e) {
				System.err.println("Cannot establish connection."+e.getMessage());
			}
	
        	
        }
        else if(command.equalsIgnoreCase("put")){
        	try {
                client.putsendResponse(filename);

            } catch (IOException e) {
                System.err.println("Cannot establish connection."+e.getMessage());
            }
        }
        else{
        	System.out.println("Please enter Correct Command");
        }
 
    }
}