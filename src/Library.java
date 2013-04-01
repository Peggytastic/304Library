
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class Library implements ActionListener {

	// command line reader
	private BufferedReader in = new BufferedReader(new InputStreamReader(
			System.in));

	static Connection con;

	// user is allowed 3 login attempts
	private int loginAttempts = 0;

	// components of the login window
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JFrame mainFrame;

	/*
	 * constructs login window and loads JDBC driver
	 */
	public Library() {

		try {
			// Load the Oracle JDBC driver
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
			System.exit(-1);
		}
		if (connect("ora_p1d7",
				"a47848080")) {
			// If the username and password are valid,
			// Remove the login window and display the library GUI
			
			showDisplay();
		}
	}

	/*
	 * connects to Oracle database named ug using user supplied username and
	 * password
	 */
	private boolean connect(String username, String password) {
		String connectURL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";

		try {
			con = DriverManager.getConnection(connectURL, username, password);
			
			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
			return false;
		}

	}

	public void actionPerformed(ActionEvent e) {
		if (connect(usernameField.getText(),
				String.valueOf(passwordField.getPassword()))) {
			// If the username and password are valid,
			// Remove the login window and display the library GUI
			mainFrame.dispose();
			showDisplay();
		} else {
			loginAttempts++;

			if (loginAttempts >= 3) {
				mainFrame.dispose();
				System.exit(-1);
			} else {
				// clear the password
				passwordField.setText("");
			}
		}
	}

	private void showDisplay() {
		LibraryGUI library = new LibraryGUI();
		library.showGUI();
	}
	
	public Connection getCon(){
		return con;
	}
	
	public static void main(String args[])
    {
		
      Library lib = new Library();
    }
}
