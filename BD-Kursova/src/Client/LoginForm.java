package Client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class LoginForm extends JPanel {
	private JTextField username;
	private JPasswordField pass;
	private JButton login;
	private JButton register;
	private CommandListener commandListener;
	
	public LoginForm(){
		Dimension dim = getPreferredSize();
		dim.width = 500;
		dim.height = 400;
		
		setPreferredSize(dim);
		
		setLayout(new GridBagLayout());
		
		username = new JTextField(10);
		pass = new JPasswordField(10);
		login = new JButton("Login");
		register = new JButton("Register");
		
		loginBehavior();
		registerBehavior();
		
		Border border = BorderFactory.createEtchedBorder();
		
		setComponents();
	}
	
	public void setCommandListener(CommandListener listener){
		this.commandListener = listener;
	}
	
	private void setComponents(){
		//Add Swing components to content pane
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(0, 0, 5, 0);
		
		//USERNAME
		gc.gridy = 0;
		
		gc.gridx = 0;
		add(new JLabel("Username: "), gc);
		
		gc.gridx = 1;
		add(username, gc);
		
		//PASSWORD
		gc.gridy++;
		
		gc.gridx = 0;
		add(new JLabel("Password"), gc);
		
		gc.gridx = 1;
		add(pass, gc);
		
		//Buttons
		gc.gridy++;
		
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		add(login, gc);
		
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 5, 0, 0);
		add(register, gc);
	}
	
	private void loginBehavior(){
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(username.getText().length() != 0 && pass.getPassword().length != 0
						&& commandListener != null){
					commandListener.command("login " + username.getText() + " " 
						+ String.valueOf(pass.getPassword()));
				}
				else{
					System.out.println("Empty fields");
				}
			}
		});
	}
	
	private void registerBehavior(){
		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(username.getText().length() != 0 && pass.getPassword().length != 0 
						&& commandListener != null){
					commandListener.command("reg " + username.getText() + " " 
						+ String.valueOf(pass.getPassword()));
				}
				else{
					System.out.println("Empty fields");
				}
			}
		});
	}

}
