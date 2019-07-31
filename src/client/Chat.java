package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Chat {
	
	private String userName, serverIP = "localhost";
	private int Port = 5000;
	private Socket sock;
	private BufferedReader reader;
	private PrintWriter writer;
	private ArrayList<String> userList = new ArrayList<String>();
	private BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
    
	public Chat() {
		
		System.out.println("UserName:");
		try {
			userName = bufferRead.readLine().toString();
		} catch (IOException e1) {}
		
		userList.add(userName);
		
		if(connection()) {
			Thread incomingReader = new Thread(new IncomingReader());
			incomingReader.start();
			Thread writerClass = new Thread(new WriterClass());
			writerClass.start();
		}	
	}
	
	public boolean connection() {
		try {
			sock = new Socket(serverIP, Port);
			InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamreader);
			
			writer = new PrintWriter(sock.getOutputStream());
			writer.println(userName + ":connect");
			writer.flush();
			
			System.out.println("Connected");
		} catch (Exception ex) {
			System.out.println("Cannot Connect! Server not found!\n");
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		new Chat();
	}

	public class IncomingReader implements Runnable {

		public void run() {
			while (true) {
				try {
					System.out.println(reader.readLine());
				} catch (IOException e) {}
			}
			
		}
	}
	
	public class WriterClass implements Runnable {
		public void run() {
			while (true) {
				String valasz = null;
				try {
					valasz = bufferRead.readLine().toString();
				} catch (IOException e) {}
				
				if (valasz.equals("exit")) {
					writer.println(userName + ":disconnect");
					writer.flush();
					System.exit(0);
				}
				
				writer.println(userName + ":chat:" + valasz);
				writer.flush();
			}
		}	
	}
		
}