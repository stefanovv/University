package Client;

import java.awt.CardLayout;
import java.awt.Container;
import java.util.Scanner;

import javax.swing.JFrame;


public class MainFrame extends JFrame {
	private LoginForm loginForm;
	private MainPanel mainPanel;
	private CommandHandler ch;
	private Container cont;
	private CardLayout cardLayout;
	private String loggedUserId;
	
	public MainFrame(String title){
		super(title);
		
		ch = new CommandHandler();
		cardLayout = new CardLayout();

		setLayout(cardLayout);
		
		//SETTING COMPONENTS
		loginForm = new LoginForm();
		mainPanel = new MainPanel();
		
		//ADDING COMPONENTS
		cont = getContentPane();
		cont.add(loginForm, "Login");
		cont.add(mainPanel, "Main");
		cardLayout.show(cont, "Login");
		
		loginForm.setCommandListener(new CommandListener() {
			public void command(String cmd) {
				System.out.println(cmd);
				String serverAnswer = ch.sendShortCommand(cmd);
				System.out.println(serverAnswer);
				
				Scanner sc = new Scanner(serverAnswer);
				String acceptingAnswer = sc.next();
				
				if(acceptingAnswer.equals("succesful")){
					String nextRead = sc.next();
					if(nextRead.equals("reg")){
						System.out.println("Login now");
					} else{
						loggedUserId = nextRead;
						
						mainPanel.setUserToChild(loggedUserId);
						if(mainPanel.getMyProfileTab() != null){
							String[] serverAnswer2 = ch.sendCommand("mytweets " + loggedUserId);
							mainPanel.getMyProfileTab().addTweets(serverAnswer2);
						}
						cardLayout.show(cont, "Main");
					}
				}
			}
		});
		
		mainPanel.setListenersToChilds(new CommandListener() {
			public void command(String cmd) {
				System.out.println(cmd);
				String[] serverAnswer = ch.sendCommand(cmd);
				
				if(serverAnswer.length == 1 && serverAnswer[0].equals("Posted")){
					if(mainPanel.getMyProfileTab() != null){
						serverAnswer = ch.sendCommand("mytweets " + loggedUserId);
						mainPanel.getMyProfileTab().addTweets(serverAnswer);
					}
				}
			}
		});
				
		setOptions();
	}	
	
	private void setOptions(){
		this.setSize(400, 600);//Zadavame mu shiro4ina i viso4ina
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Kogato se natisne X da se zatvarq
		this.setVisible(true);//Za da se vijda - default e false
	}
	
}
