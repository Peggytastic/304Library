import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
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
		frame.setBackground(Color.white);
		frame.setPreferredSize(new Dimension(950, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Panel for viewing the tables
		tablePane = new TablePane(); 
		tablePane.setBorder(BorderFactory.createLineBorder(Color.black));

		// Panel for displaying types of users
		userPane = new JPanel(); 
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

		JMenu Library;
		JMenuItem quit;

		Library = new JMenu("Library");

		// Exits the application
		quit = new JMenuItem("Quit");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame.dispose();
			}
		});

		Library.add(quit);

		menuBar = new JMenuBar();
		menuBar.add(Library);

		frame.setJMenuBar(menuBar);
	}

	// Method for initializing the activities for each user
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

	public static void showTable(ResultSet rs, String buttonClicked) {

		int numCols;
		ResultSetMetaData rsmd;

		JTextArea tableTitle = null;
		JTable table = null;

		try {

				rsmd = rs.getMetaData();
			
			numCols = rsmd.getColumnCount();

			String columnNames[] = new String[numCols];
			for (int i = 0; i < numCols; i++) {
				columnNames[i] = rsmd.getColumnName(i + 1);
			}

			if (buttonClicked == "addBorrowerButton") {

				// For creating the size of the table
				Statement stmt = Library.con.createStatement();
				ResultSet count = stmt.executeQuery("SELECT * FROM Borrower");
				List<Integer> borrowers = new ArrayList<Integer>();
				while (count.next()) {
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
				
				// Fill table
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

			if (buttonClicked == "addBookButton") {

				// For creating the size of the table
				Statement stmt = Library.con.createStatement();
				ResultSet count = stmt.executeQuery("SELECT * FROM Book");
				List<String> books = new ArrayList<String>();
				while (count.next()) {
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
				
				// Fill table
				while (rs.next()) {
					callNumber = rs.getString("callNumber");
					isbn = rs.getString("isbn");
					title = rs.getString("title");
					mainAuthor = rs.getString("mainAuthor");

					publisher = rs.getString("publisher");
					year = rs.getInt("year");

					Object tuple[] = { callNumber, isbn, title, mainAuthor,
							publisher, year };
					data[j] = tuple;
					j++;

				}
				tableTitle = new JTextArea("Book table");
				table = new JTable(data, columnNames);
			}

			if (buttonClicked == "addBookCopyButton"  || buttonClicked == "processReturnButton") {

				// For creating the size of the table
				Statement stmt = Library.con.createStatement();
				ResultSet count = stmt.executeQuery("SELECT * FROM BookCopy");
				List<String> bookCopies = new ArrayList<String>();
				while (count.next()) {
					bookCopies.add(count.getString("callNumber"));
				}
				Object data[][] = new Object[bookCopies.size()][numCols];
				count.close();

				String callNumber;
				String copyNo;
				String status;
				int j = 0;
				
				// Fill table
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

			if (buttonClicked == "checkoutButton") {

				// For creating the size of the table
				Statement stmt = Library.con.createStatement();
				ResultSet count = stmt.executeQuery("SELECT * FROM borrowing");
				List<String> borrowings = new ArrayList<String>();
				while (count.next()) {
					borrowings.add(count.getString("callNumber"));
				}
				Object data[][] = new Object[borrowings.size()][numCols];
				count.close();

				int borid;
				int bid;
				String callNumber;
				String copyNo;
				Date outDate;
				Date inDate;
				int j = 0;
				
				// Fill table
				while (rs.next()) {
					borid = rs.getInt("borid");
					bid = rs.getInt("bid");
					callNumber = rs.getString("callNumber");
					copyNo = rs.getString("copyNo");
					outDate = rs.getDate("outDate");
					inDate = rs.getDate("inDate");
					Object tuple[] = { borid, bid, callNumber, copyNo, outDate, inDate };
					data[j] = tuple;
					j++;

				}
				tableTitle = new JTextArea("Borrowing table");
				table = new JTable(data, columnNames);

			}
			
			
			table.setEnabled(false);
			JScrollPane scrollPane = new JScrollPane(table);
			table.setAutoCreateRowSorter(true);

			// Display table
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
	public static void showAccountTables(ResultSet rs1, ResultSet rs2, ResultSet rs3, int bid) {
		// Tables won't set size properly!!!
		
		int numCols1;
		int numCols2;
		int numCols3;
		ResultSetMetaData rsmd1;
		ResultSetMetaData rsmd2;
		ResultSetMetaData rsmd3;

		JTextArea tableTitle1 = new JTextArea("Checked out books");
		JTextArea tableTitle2 = new JTextArea("Outstanding fines");
		JTextArea tableTitle3 = new JTextArea("Books on hold");

		try {

			rsmd1 = rs1.getMetaData();
			rsmd2 = rs2.getMetaData();
			rsmd3 = rs3.getMetaData();
			
			numCols1 = rsmd1.getColumnCount();
			numCols2 = rsmd2.getColumnCount();
			numCols3 = rsmd3.getColumnCount();

			// Get column names for checked out table
			String columnNames1[] = new String[numCols1];
			for (int i = 0; i < numCols1; i++) {
				columnNames1[i] = rsmd1.getColumnName(i + 1);
			}
			
			// Get column names for fines table
			String columnNames2[] = new String[numCols2];
			for (int i = 0; i < numCols2; i++) {
				columnNames2[i] = rsmd2.getColumnName(i + 1);
			}
			
			// Get column names for holds table
			String columnNames3[] = new String[numCols3];
			for (int i = 0; i < numCols3; i++) {
				columnNames3[i] = rsmd3.getColumnName(i + 1);
			}
			
			
			// Get table sizes
			PreparedStatement ps1 = Library.con.prepareStatement("select borrowing.borid, bookcopy.callNumber, bookcopy.copyNo, borrowing.outDate, borrowing.inDate from Borrowing, BookCopy where Borrowing.callNumber=BookCopy.callNumber and Borrowing.copyNo=BookCopy.CopyNo and BookCopy.Status = 'out' and Borrowing.bid = ?");
			ps1.setInt(1, bid);
			ps1.executeQuery();
			
			PreparedStatement ps2 = Library.con.prepareStatement("Select fid, amount, issuedDate from Fine WHERE paidDate is NULL and borid in (select borrowing.borid from Borrowing, BookCopy where Borrowing.callNumber = BookCopy.callNumber and Borrowing.copyNo = BookCopy.copyNo and Borrowing.bid = ?)");
			ps2.setInt(1, bid);
			ps2.executeQuery();
			
			PreparedStatement ps3 = Library.con.prepareStatement("select holdrequest.hid, holdrequest.issuedDate, Book.callNumber, Book.isbn, Book.title from Book INNER JOIN HoldRequest on Book.callNumber = HoldRequest.callNumber where HoldRequest.bid = ?");
			ps3.setInt(1, bid);
			ps3.executeQuery();
			
			List<String> checkedOut = new ArrayList<String>();
			ResultSet count1 = ps1.getResultSet();
			while (count1.next()) {
				checkedOut.add(count1.getString("borid"));
			}
			Object data1[][] = new Object[checkedOut.size()][numCols1];
			count1.close();
			
			List<String> fines = new ArrayList<String>();
			ResultSet count2 = ps2.getResultSet();
			while (count2.next()) {
				fines.add(count2.getString("amount"));
			}
			Object data2[][] = new Object[fines.size()][numCols2];
			count2.close();
			
			List<String> holds = new ArrayList<String>();
			ResultSet count3 = ps3.getResultSet();
			while (count3.next()) {
				holds.add(count3.getString("callNumber"));
			}
			Object data3[][] = new Object[holds.size()][numCols3];
			count3.close();
			
			int borid;
			String callNumber;
			String copyNo;
			Date outDate;
			Date inDate;
			
			int j = 0;
			
			// Fill checked out table
			while (rs1.next()) {
				borid = rs1.getInt("borid");
				callNumber = rs1.getString("callNumber");
				copyNo = rs1.getString("copyNo");
				outDate = rs1.getDate("outDate");
				inDate = rs1.getDate("inDate");
				Object tuple[] = { borid, callNumber, copyNo, outDate, inDate };
				data1[j] = tuple;
				j++;

			}
			
			int fid;
			String amount;
			Date issuedDate;
			
			j=0;
			
			// Fill fines table
			while (rs2.next()) {
				fid = rs2.getInt("fid");
				amount = rs2.getString("amount");
				issuedDate = rs2.getDate("issuedDate");
				Object tuple[] = { fid, amount, issuedDate };
				data2[j] = tuple;
				j++;

			}
			
			int hid;
			Date issuedDate2;
			String isbn;
			String callNumber2;
			String title;
			
			
			j=0;
			
			// Fill holds table
			while (rs3.next()) {
				hid = rs3.getInt("hid");
				issuedDate2 = rs3.getDate("issuedDate");
				isbn = rs3.getString("isbn");
				callNumber2 = rs3.getString("callNumber");
				title = rs3.getString("title");
				Object tuple[] = { hid, issuedDate2, isbn, callNumber2, title };
				data2[j] = tuple;
				j++;

			}
			rs1.close();
			rs2.close();
			rs3.close();
			
			// View tables
			JTable checkedOutTable = new JTable(data1, columnNames1);
			checkedOutTable.setEnabled(false);
			JScrollPane scrollPane1 = new JScrollPane(checkedOutTable);
			checkedOutTable.setAutoCreateRowSorter(true);
			
			JTable fineTable = new JTable(data2, columnNames2);
			fineTable.setEnabled(false);
			JScrollPane scrollPane2 = new JScrollPane(fineTable);
			fineTable.setAutoCreateRowSorter(true);

			JTable holdsTable = new JTable(data3, columnNames3);
			holdsTable.setEnabled(false);
			JScrollPane scrollPane3 = new JScrollPane(holdsTable);
			holdsTable.setAutoCreateRowSorter(true);

			
			fineTable.setFillsViewportHeight(true);
			
			tablePane.removeAll();
			tablePane.updateUI();
			tableTitle1.setEditable(false);
			tablePane.add(tableTitle1);
			tablePane.add(scrollPane1);
			
			tableTitle2.setEditable(false);
			tablePane.add(tableTitle2);
			tablePane.add(scrollPane2);
			
			tableTitle3.setEditable(false);
			tablePane.add(tableTitle3);
			tablePane.add(scrollPane3);
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}