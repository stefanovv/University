package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ClientThread implements Runnable {
	private Socket socket;
	private BufferedReader input;
	private DataOutputStream output;
	private Database db;
	
	public ClientThread(Socket client, Database db) {
		this.socket = client;
		this.db = db;
		
		if(this.socket != null){
			try {
				output = new DataOutputStream(this.socket.getOutputStream());
			} catch (IOException e) {
				System.out.println("Error setting output stream");
			}
			
			try {
				input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			} catch (IOException e) {
				System.out.println("Error setting input stream");
			}
		}
	}

	public void run() {
		while(!this.socket.isClosed()){
			String readed = null;
			
			try {
				readed = input.readLine();
			} catch (IOException e) {
				System.out.println("Error with reading in server thread");
				input = null;
			} catch (NullPointerException e){
				System.out.println("Connection has been closed");
				try {
					this.socket.close();
				} catch (IOException e1) {
					System.out.println("Error closing socket from server");
				}
			}

			if(readed != null){
				try{
					Scanner sc = new Scanner(readed);
					if(sc.hasNext()){
						String cmd = sc.next();//Vzimane na jelanoto deistvie ot komandata
						StringBuilder sb;
						String sqlQuery;
						int affectedRows;
						ResultSet results = null;
						String userId = null;
						
						switch(cmd){
							case "login":
								String logUsername = sc.next();
								String logPassword = sc.next();
								System.out.println("user: " + logUsername + " is loggin in");
								
								sqlQuery = "SELECT id FROM " + db.getDatabaseName() + ".users "
										+ "WHERE username=? AND password=?;";
								
								results = db.selectQuery(sqlQuery, logUsername, logPassword);
								try {
									results.next();
									if(results.getInt("id") > 0){
										output.writeBytes("succesful " + results.getInt("id"));
									}
									
								} catch (SQLException e) {
									output.writeBytes("Wrong username or password");
								}

								output.writeBytes("\n");
								output.flush();
								break;
							case "reg":
								String regUsername = sc.next();
								String regPassword = sc.next();
								System.out.println("user: " + regUsername + " is registering");
								
								sqlQuery = "INSERT INTO "+ db.getDatabaseName() + ".users(username, password)"
										+ " VALUES(?,?);";
								affectedRows = db.query(sqlQuery, regUsername, regPassword);
								
								if(affectedRows != 0){
									output.writeBytes("succesful reg");
								} else{
									output.writeBytes("Username already exists");
								}
				
								output.writeBytes("\n");
								output.flush();
								break;
							case "post":
								userId = sc.next();
								System.out.println("posting user: " + userId);
								sb = new StringBuilder();
								
								while(sc.hasNext()){
									sb.append(sc.next() + " ");
								}
								
								System.out.println(sb.toString());
								
								sqlQuery = "INSERT INTO "+ db.getDatabaseName() + ".tweets(tweet, userId)"
										+ " VALUES(?,?);";
								affectedRows = db.query(sqlQuery, sb.toString().trim(), userId);
								
								if(affectedRows != 0){
									output.writeBytes("Posted");
									output.writeBytes("\n");
									output.flush();
								} else{
									output.writeBytes("Error with posting");
								}
								
								output.writeBytes("endSending");
								output.writeBytes("\n");
								output.flush();
								break;
							case "mytweets":
								userId = sc.next();
								
								sqlQuery = "SELECT tweet,posted FROM " + db.getDatabaseName() 
										+ ".tweets WHERE userId=?";
								
								results = db.selectQuery(sqlQuery, userId);
								
								try {
									while(results.next()){
										output.writeBytes(results.getString("posted") + " " + results.getString("tweet"));
										output.writeBytes("\n");
										output.flush();
									}
									
									output.writeBytes("endSending");
									output.writeBytes("\n");
									output.flush();
								} catch (SQLException e) {
									db.printSqlException(e);
									System.out.println("Tweets get error");
								}
									
								break;
							default:
								output.writeBytes("Error in received command");
								output.writeBytes("\n");
								output.flush();
								break;
						}
					}
					sc.close();
				} catch(IOException e){
					System.out.println("Error with returning command");
				}
			}
		}
	}
}
