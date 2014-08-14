package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class CommandHandler{
	private String cmd;
	private Socket socket = null;
	private DataOutputStream output;
	private BufferedReader input;
	
	public CommandHandler(){
		
		try {
			this.socket = new Socket("localhost", 4321);
		} catch (UnknownHostException e) {
			System.out.println("Host not found");
		} catch (IOException e) {
			System.out.println("Error with connecting to host");
		}
		
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

	public String sendShortCommand(String cmd) {
		this.cmd = cmd;
		
		if(this.socket != null){
			try {
				output.writeBytes(this.cmd);
				output.writeBytes("\n");
				output.flush();
				
				System.out.println("data sedned");
				
				String answer = input.readLine();
				
				return answer;
			} catch (IOException e) {
				System.out.println("Error sending command and receiving answer");
			}
		}
		return "Server not setted up";
	}
	
	public String[] sendCommand(String cmd){
		ArrayList<String> list = new ArrayList<String>();
		
		this.cmd = cmd;
		
		if(this.socket != null){
			try {
				output.writeBytes(this.cmd);
				output.writeBytes("\n");
				output.flush();
				
				System.out.println("data sedned");
				String answer = null;
				answer = input.readLine();
				
				while(!answer.equals("endSending")){
					System.out.println(answer);
					
					list.add(answer);
					answer = input.readLine();
				}
				
			} catch (IOException e) {
				System.out.println("Error sending command and receiving answer");
			}
		}
		
		String[] result = new String[list.size()];
		result = list.toArray(result);
		
		return result;
	}
	
	public void socketClose(){
		try {
			this.socket.close();
		} catch (IOException e) {
			System.out.println("Error closing socket");
		}
	}
}
