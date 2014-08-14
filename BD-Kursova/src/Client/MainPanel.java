package Client;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainPanel extends JPanel {
	private JTabbedPane tabbedPane;
	private HomeTab home;
	private MyProfileTab myProfile;
	private BorderLayout bl;
	
	public MainPanel(){
		
		setLayout(new BorderLayout());
		
		tabbedPane = new JTabbedPane();
		home = new HomeTab(); 
		myProfile = new MyProfileTab();
		
		tabbedPane.add(home, "Home");
		tabbedPane.add(myProfile, "My profile");
		
		add(tabbedPane, BorderLayout.CENTER);
	}
	
	public void setListenersToChilds(CommandListener listener){
		this.myProfile.setCommandListener(listener);
	}
	
	public MyProfileTab getMyProfileTab(){
		return this.myProfile;
	}
	
	public void setUserToChild(String user){
		this.myProfile.setUser(user);
	}
}

