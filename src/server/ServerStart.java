package server;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerStart implements Runnable {

	ArrayList clientOutputStreams;
	
	public void run() {
	    clientOutputStreams = new ArrayList();
	
	    try {
	    	ServerSocket serverSock = new ServerSocket(5000);
	
	    	while (true) {
				// set up the server writer function and then begin at the same
					// the listener using the Runnable and Thread
				Socket clientSock = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
				clientOutputStreams.add(writer);
				
				// use a Runnable to start a 'second main method that will run
				// the listener
				Thread listener = new Thread(new ClientHandler(clientSock, writer));
				listener.start();
				System.out.println("Got a connection. \n");
	    	} // end while
	    } catch (Exception ex) {
	    	System.out.println("Error making a connection. \n");
		} // end catch
	
	} // end go()
}