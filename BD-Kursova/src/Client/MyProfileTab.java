package Client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MyProfileTab extends JPanel {
	private JTextArea postTweet;
	private JButton logout;
	private JButton post;
	private JPanel buttonBar;
	private JPanel tweetsPanel;
	private JScrollPane scroller;
	private JScrollPane textAreaScroll;
	private CommandListener commandListener;
	private String userId;
	private GridBagConstraints gc;
	
	public MyProfileTab(){
		
		//Setting components
		postTweet = new JTextArea(2, 30);
		tweetsPanel = new JPanel();
		logout = new JButton("Logout");
		post = new JButton("  Post  ");
		buttonBar = new JPanel();
		
		tweetsPanel.setLayout(new BoxLayout(tweetsPanel, BoxLayout.PAGE_AXIS));
		Dimension dim = tweetsPanel.getPreferredSize();
		dim.width = 400;
		dim.height = 300;
		tweetsPanel.setPreferredSize(dim);
		scroller = new JScrollPane(tweetsPanel);
		scroller.setPreferredSize(dim);
		
		postTweet.setBorder(BorderFactory.createTitledBorder("Post tweet"));
		dim = postTweet.getPreferredSize();
		dim.width = 300;
		dim.height = 100;
		postTweet.setPreferredSize(dim);
			
		tweetsPanel.setBorder(BorderFactory.createTitledBorder("My tweets"));
		
		post.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(userId != null){
					if(postTweet.getText().length() != 0 && commandListener != null){
						commandListener.command("post " + userId + " " + postTweet.getText());
						postTweet.setText("");
					}
					else{
						System.out.println("Empty fields");
					}
				} else{
					System.out.println("User not set");
				}
			}
		});
		
		layoutComponents();
	}
	
	public void setCommandListener(CommandListener listener){
		this.commandListener = listener;
	}
	
	private void layoutComponents(){
		setLayout(new BorderLayout());
		
		buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.PAGE_AXIS));
		logout.setAlignmentX(Component.CENTER_ALIGNMENT);
		post.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonBar.add(logout);
		buttonBar.add(post);
	
		textAreaScroll = new JScrollPane(postTweet);
		textAreaScroll.setPreferredSize(new Dimension(250, 100));
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
		
		top.add(postTweet);
		top.add(buttonBar);
		
		add(top, BorderLayout.NORTH);
		add(tweetsPanel, BorderLayout.CENTER);
	}
	
	public void addTweets(String[] tweets){
		this.tweetsPanel.removeAll();
		
		for(String fullTweet: tweets){
			Scanner sc = new Scanner(fullTweet);
			String posted = sc.next();
			
			StringBuilder sb = new StringBuilder();
			while(sc.hasNext()){
				sb.append(sc.next() + " ");
			}
			
			JTextArea tweetArea = new JTextArea();
			tweetArea.setBorder(BorderFactory.createTitledBorder(posted));
			tweetArea.setText(sb.toString().trim());
			tweetArea.setBackground(SystemColor.info);
			tweetArea.setEditable(false);
			
			tweetsPanel.add(tweetArea);
		}
		
		this.remove(tweetsPanel);
		this.add(tweetsPanel, BorderLayout.CENTER);
	}
	
	public void setUser(String userId){
		this.userId = userId;
	}
	
}
