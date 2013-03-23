import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class LibraryGUI {
	// The main frame for the GUI
	static JFrame frame;

	// The menubar for the GUI
	private JMenuBar menuBar;

	// All the panels within the frame
	private JPanel userPane;
	public static TablePane tablePane;
	private ActivitiesPane activitiesPane;

	// Constructor for GUI
	public LibraryGUI() {

	}

	// Method to display the GUI
	public void showGUI() {

		frame = new JFrame("Library");
		frame.setBackground(Color.white); // Sets background color of the frame
											// to white
		frame.setPreferredSize(new Dimension(950, 600)); // Sets the size of the
															// frame - frame
															// size can still be
															// increased and
															// decreased by user
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tablePane = new TablePane(); // Panel for viewing a table
		tablePane.setBorder(BorderFactory.createLineBorder(Color.black));

		userPane = new JPanel(); // Panel for displaying types of users
		userPane.setLayout(new BoxLayout(userPane, BoxLayout.Y_AXIS));
		userPane.setBorder(BorderFactory.createLineBorder(Color.black));
		Font font = new Font("Arial", Font.BOLD, 16);
		JTextArea users = new JTextArea("Users");
		users.setFont(font);
		users.setEditable(false);
		users.setMaximumSize(new Dimension(700, 30));
		userPane.add(users);

		activitiesPane = new ActivitiesPane();
		activitiesPane.setBorder(BorderFactory.createLineBorder(Color.black));

		initializeMenu();
		initializeUserPane();

		// Adds all the panels to the frame
		frame.getContentPane().add(userPane, BorderLayout.WEST);
		frame.getContentPane().add(tablePane, BorderLayout.CENTER);

		frame.getContentPane().add(activitiesPane, BorderLayout.EAST);

		// Shows the frame
		frame.pack();
		frame.setVisible(true);
	}

	// Method for initializing the menu
	private void initializeMenu() {

		JMenu photoLibrary;
		JMenuItem quit;

		photoLibrary = new JMenu("Library");

		// Exits the application
		quit = new JMenuItem("Quit");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame.dispose();
			}
		});

		photoLibrary.add(quit);

		menuBar = new JMenuBar();
		menuBar.add(photoLibrary);

		frame.setJMenuBar(menuBar);
	}

	// Method for initializing the activities
	private void initializeUserPane() {

		JButton clerkButton = new JButton("Clerk");
		JButton borrowerButton = new JButton("Borrower");
		JButton librarianButton = new JButton("Librarian");

		userPane.add(clerkButton);
		userPane.add(borrowerButton);
		userPane.add(librarianButton);

		clerkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				activitiesPane.removeAll();
				activitiesPane.updateUI();
				activitiesPane.display("Clerk");
			}

		});
		borrowerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				activitiesPane.removeAll();
				activitiesPane.updateUI();
				activitiesPane.display("Borrower");
			}

		});
		librarianButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				activitiesPane.removeAll();
				activitiesPane.updateUI();
				activitiesPane.display("Librarian");
			}

		});

	}

	public static void showTable(ResultSet rs, PreparedStatement ps,
			String buttonClicked) {

		int numCols;

		ResultSetMetaData rsmd;	
		
		JTextArea tableTitle = null;
		JTable table = null;

		try {

			if (rs != null){
			rsmd = rs.getMetaData();
			}
			
			else{
				rsmd = ps.getMetaData();
			}
			numCols = rsmd.getColumnCount();

			String columnNames[] = new String[numCols];
			for (int i = 0; i < numCols; i++) {
				columnNames[i] = rsmd.getColumnName(i + 1);
			}

			if (buttonClicked == "addBorrowerButton") {
				Statement stmt = Library.con.createStatement();
				ResultSet count = stmt.executeQuery("SELECT * FROM Borrower");
				List<Integer> borrowers = new ArrayList<Integer>();
				while(count.next()){
				 borrowers.add(count.getInt("bid"));
				}
				Object data[][] = new Object[borrowers.size()][numCols];
				count.close();
				
				int bid;
				String password;
				String name;
				String address;
				int phone;
				String emailAddress;
				int sinOrStNo;
				String type;
				String expiryDate;
				int j = 0;
				while (rs.next()) {
					bid = rs.getInt("bid");
					password = rs.getString("password");
					name = rs.getString("name");
					address = rs.getString("address");
					phone = rs.getInt("phone");
					emailAddress = rs.getString("emailAddress");
					sinOrStNo = rs.getInt("sinOrStNo");
					type = rs.getString("type");
					expiryDate = rs.getString("expiryDate");
					Object tuple[] = { bid, password, name, address, phone,
							emailAddress, sinOrStNo, type, expiryDate };
					data[j] = tuple;
					j++;

				}
				tableTitle = new JTextArea("Borrower table");
				table = new JTable(data, columnNames);
				
			}
			
			if(buttonClicked == "addBookButton"){
				Statement stmt = Library.con.createStatement();
				ResultSet count = stmt.executeQuery("SELECT * FROM Book");
				List<String> books = new ArrayList<String>();
				while(count.next()){
				 books.add(count.getString("callNumber"));
				}
				Object data[][] = new Object[books.size()][numCols];
				count.close();
				
				String callNumber;
				String isbn;
				String title;
				String mainAuthor;
				String publisher;
				int year;
				int j = 0;
				while (rs.next()) {
					callNumber = rs.getString("callNumber");
					isbn = rs.getString("isbn");
					title = rs.getString("title");
					mainAuthor = rs.getString("mainAuthor");
					
					publisher = rs.getString("publisher");
					year = rs.getInt("year");

					Object tuple[] = { callNumber, isbn, title, mainAuthor, publisher, year };
					data[j] = tuple;
					j++;

				}
				tableTitle = new JTextArea("Book table");
				table = new JTable(data, columnNames);
			}
			
			if(buttonClicked == "addBookCopyButton"){
				Statement stmt = Library.con.createStatement();
				ResultSet count = stmt.executeQuery("SELECT * FROM BookCopy");
				List<String> bookCopies = new ArrayList<String>();
				while(count.next()){
				 bookCopies.add(count.getString("callNumber"));
				}
				Object data[][] = new Object[bookCopies.size()][numCols];
				count.close();
				
				String callNumber;
				String copyNo;
				String status;
				int j = 0;
				while (rs.next()) {
					callNumber = rs.getString("callNumber");
					copyNo = rs.getString("copyNo");
					status = rs.getString("status");
					Object tuple[] = { callNumber, copyNo, status };
					data[j] = tuple;
					j++;

				}
				tableTitle = new JTextArea("BookCopy table");
				table = new JTable(data, columnNames);
				
			}
			
			table.setEnabled(false);
			JScrollPane scrollPane = new JScrollPane(table);
			table.setAutoCreateRowSorter(true);
			
			table.setFillsViewportHeight(true);
			tablePane.removeAll();
			tablePane.updateUI();
			tableTitle.setEditable(false);
			tablePane.add(tableTitle);
			tablePane.add(scrollPane);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}