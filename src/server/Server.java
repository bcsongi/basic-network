package server;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {

	private static ArrayList<PrintWriter> clientOutputStreams;
	private ServerSocket serverSock;
	
	public void run() {
	    clientOutputStreams = new ArrayList<PrintWriter>();
	    System.out.println("Server started. \n");
    	
	    try {
	    	serverSock = new ServerSocket(5000);
	
	    	while (true) {
				Socket clientSock = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
				
				clientOutputStreams.add(writer);
			
				Thread listener = new Thread(new ClientHandler(clientSock, writer));
				listener.start();
				
				System.out.println("Got a connection. \n Online userS:");
				
				ArrayList<String> onlineUsers = ClientHandler.getOnlineUsers();
			    for (String name : onlineUsers) {
			    	System.out.println(name);
			    }
	    	} 
	    } catch (Exception ex) {
	    	System.out.println(ex +  " + Error making a connection. \n");
		}   
	}

	public static ArrayList<PrintWriter> getClientOutputStreams() {
		return clientOutputStreams;
	}
	
	public static void removeClientOutputStreams(PrintWriter client) {
		clientOutputStreams.remove(client);
	}	
	
    public static void main(String[] args) {
    	Server s = new Server();
        s.run();
    }
    
}
