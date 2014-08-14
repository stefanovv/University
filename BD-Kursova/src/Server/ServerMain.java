package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class ServerMain {
	
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(4321);
			System.out.println("Server started");
			
			Database db = new Database("localhost", "twitter", "root", "");
			try {
				db.connect();
			} catch (SQLException excep) {
				db.printSqlException(excep);
			}
			while (true) {
				try {
					
					Socket clientSocket = serverSocket.accept();
					Thread client = new Thread(new ClientThread(clientSocket, db));
					client.start();
				} catch (IOException e) {
					System.out.println("Erorr accepting client");
				}
			}
		} catch (IOException e) {
			System.out.println("Error with starting the server socked");
		} finally {
			if(serverSocket != null){
				serverSocket.close();
			}
		}
	}
}
