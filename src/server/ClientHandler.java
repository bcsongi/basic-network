package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ClientHandler implements Runnable	{

	private BufferedReader reader;
	private PrintWriter client;
    private static ArrayList<String> onlineUsers = new ArrayList<String>();

	public ClientHandler(Socket socket, PrintWriter client) {
		this.client = client;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (Exception ex) {
			System.out.println("Error beginning StreamReader. \n");
		} 
	} 

	public void run() {
        String message, connect = "connect", disconnect = "disconnect", chat = "chat" ;
		String[] data;

		try {
			while ((message = reader.readLine()) != null) {
				System.out.println("Received: " + message + "\n");
				data = message.split(":");

	            if (data[1].equals(connect)) {
                    tellEveryone(data[0] + " kapcsolodott \n");
                    userAdd(data[0]);
				} else if (data[1].equals(disconnect)) {
                    tellEveryone((data[0] + " lecsatlakozott \n"));
                    userRemove(data[0]);
				} else if (data[1].equals(chat)) {
					tellEveryone(message);
				} else {
					System.out.println("No Conditions were met. \n");
                }
		     }
		} 
		catch (Exception ex) {
			System.out.println("Lost a connection. \n");
            Server.removeClientOutputStreams(client);
		}
	}
	
    public void tellEveryone(String message) {
    	Iterator<PrintWriter> it = Server.getClientOutputStreams().iterator();

		while (it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
			   // System.out.println("Elkuld mindenkinek: " + writer + "\n");
	            writer.flush();
	        }
			catch (Exception ex) {
				System.out.println("Error telling everyone. \n");
			} 
		}
    }

	public void userAdd (String data) {
		onlineUsers.add(data);
	}

	public void userRemove (String data) {
        onlineUsers.remove(data);
	}	
	
	public static ArrayList<String> getOnlineUsers() {
		return onlineUsers;
	}
	
}