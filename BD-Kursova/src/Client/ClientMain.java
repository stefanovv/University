package Client;

import javax.swing.SwingUtilities;

public class ClientMain {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainFrame("Twitter");
				} catch (Exception e) {
					System.out.println("Error starting application");
				}
			}
		});
	}
}
