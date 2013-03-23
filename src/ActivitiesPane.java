import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ActivitiesPane extends JPanel {
	public ActivitiesPane() {

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

	}

	public void display(String user) {

		Font font = new Font("Arial", Font.BOLD, 16);
		JTextArea title = new JTextArea("Transactions");
		title.setFont(font);

		title.setEditable(false);

		title.setMaximumSize(new Dimension(700, 30));

		this.add(title);

		if (user == "Clerk") {
			JButton addBorrowerButton = new JButton("Add borrower");
			JButton checkoutButton = new JButton("Check-out items");
			JButton processReturnButton = new JButton("Process return");
			JButton checkOverdueButton = new JButton("Check overdue items");

			addBorrowerButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addBorrower();
				}

			});

			checkoutButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					checkout();
				}
			});

			processReturnButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					returnBooks();
				}
			});
			
			this.add(addBorrowerButton);
			this.add(checkoutButton);
			this.add(processReturnButton);
			this.add(checkOverdueButton);
		}

		if (user == "Borrower") {

			JButton searchButton = new JButton("Search for books");
			JButton checkAccountButton = new JButton("Check account");
			JButton placeHoldButton = new JButton("Place a hold");
			JButton payFineButton = new JButton("Pay fine");

			this.add(searchButton);
			this.add(checkAccountButton);
			this.add(placeHoldButton);
			this.add(payFineButton);

		}

		if (user == "Librarian") {

			JButton addBookButton = new JButton("Add book");
			JButton addBookCopyButton = new JButton("Add book copy");
			JButton genReportCheckoutButton = new JButton(
					"Generate report for all checked out books");
			JButton genReportPopularButton = new JButton(
					"Generate report for popular books");

			addBookButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					addBook();
				}
			});

			addBookCopyButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					addBookCopy();
				}
			});
			this.add(addBookButton);
			this.add(addBookCopyButton);
			this.add(genReportCheckoutButton);
			this.add(genReportPopularButton);

		}

	}

	public void addBorrower() {
		
		// User inputs: password, name, address, phone, email, sin/st, type, date
		JTextField passwordField = new JTextField(10);
		JTextField nameField = new JTextField(10);
		JTextField addressField = new JTextField(10);
		JTextField phoneField = new JTextField(10);
		JTextField emailAddressField = new JTextField(10);
		JTextField sinOrStNoField = new JTextField(10);

		String[] typeString = { "Student", "Faculty", "Staff" };
		JComboBox typeCombo = new JComboBox(typeString);
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		java.util.Date date2 = new java.util.Date();

		String currentYear = dateFormat.format(date2).toString();
		String[] yearString = {
				"Year",
				currentYear,
				Integer.toString(((Integer.valueOf(currentYear)).intValue() + 1)),
				Integer.toString(((Integer.valueOf(currentYear)).intValue() + 2)),
				Integer.toString(((Integer.valueOf(currentYear)).intValue() + 3)),
				Integer.toString(((Integer.valueOf(currentYear)).intValue() + 4)) };
		String[] monthString = { "Month", "01", "02", "03", "04", "05", "06",
				"07", "08", "09", "10", "11", "12" };
		String[] dayString = { "Day", "01", "02", "03", "04", "05", "06", "07",
				"08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
				"18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
				"28", "29", "30", "31" };
		JComboBox yearCombo = new JComboBox(yearString);
		JComboBox monthCombo = new JComboBox(monthString);
		JComboBox dayCombo = new JComboBox(dayString);
		JPanel expiryPanel = new JPanel();
		FlowLayout expiryLayout = new FlowLayout();

		expiryPanel.setLayout(expiryLayout);
		expiryPanel.add(yearCombo);
		expiryPanel.add(monthCombo);
		expiryPanel.add(dayCombo);

		JComponent[] inputs = new JComponent[] {

		new JLabel("Password:"), passwordField, new JLabel("Name:"), nameField,
				new JLabel("Address:"), addressField, new JLabel("Phone:"),
				phoneField, new JLabel("Email:"), emailAddressField,
				new JLabel("Sin or St No.:"), sinOrStNoField,
				new JLabel("Type:"), typeCombo, new JLabel("Expiration date"),
				expiryPanel,

		};
		int result = JOptionPane.showConfirmDialog(null, inputs,
				"Enter borrower info", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {

			String password = passwordField.getText();
			String name = nameField.getText();
			String address = addressField.getText();
			int phone = Integer.parseInt(phoneField.getText());
			String emailAddress = emailAddressField.getText();
			int sinOrStNo = Integer.parseInt(sinOrStNoField.getText());
			String type = typeCombo.getSelectedItem().toString().toLowerCase();
			Date expiryDate = null;

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String date = yearCombo.getSelectedItem().toString() + "-"
					+ monthCombo.getSelectedItem().toString() + "-"
					+ dayCombo.getSelectedItem().toString();

			// TODO: add error popup
			try {
				expiryDate = new Date(df.parse(date).getTime());
			} catch (ParseException ex) {
				new ErrorMessage("dkfjdf");
			}

			PreparedStatement ps;

			try {
				// Insert new borrower into Borrower table
				ps = Library.con
						.prepareStatement("INSERT INTO borrower (password, name, address, phone, emailAddress, sinOrStNo, type, expiryDate) VALUES (?,?,?,?,?,?,?,?)");

				ps.setString(1, password);
				ps.setString(2, name);
				ps.setString(3, address);
				ps.setInt(4, phone);
				ps.setString(5, emailAddress);
				ps.setInt(6, sinOrStNo);
				ps.setString(7, type);

				ps.setDate(8, expiryDate);
				ps.executeUpdate();
				Library.con.commit();

				// Show Borrower table
				Statement stmt = Library.con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM Borrower");
				LibraryGUI.showTable(rs, "addBorrowerButton");
				
				rs.close();
				ps.close();
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void addBook() {
		
		// User inputs: callNumber, isbn, title, mainAuthors, otherAuthors, publisher, year, subjects
		JTextField callNumberField = new JTextField(15);
		JTextField isbnField = new JTextField(10);
		JTextField titleField = new JTextField(10);
		JTextField mainAuthorField = new JTextField(10);
		JTextField otherAuthorsField = new JTextField(100);
		JTextField publisherField = new JTextField(10);
		JTextField yearField = new JTextField(10);
		JTextField subjectsField = new JTextField(100);

		JComponent[] inputs = new JComponent[] {

		new JLabel("Call number:"), callNumberField, new JLabel("isbn:"),
				isbnField, new JLabel("Title:"), titleField,
				new JLabel("Main author:"), mainAuthorField,
				new JLabel("Other authors:"), otherAuthorsField,
				new JLabel("Publisher:"), publisherField, new JLabel("Year:"),
				yearField, new JLabel("Subjects:"), subjectsField

		};
		int result = JOptionPane.showConfirmDialog(null, inputs,
				"Enter book info", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			String callNumber = callNumberField.getText();
			String isbn = isbnField.getText();
			String title = titleField.getText();
			String mainAuthor = mainAuthorField.getText();
			String[] otherAuthors = otherAuthorsField.getText().split(",");
			String publisher = publisherField.getText();
			int year = Integer.parseInt(yearField.getText());
			String[] subjects = subjectsField.getText().split(",");

			try {
				
				// Insert new book into Book table
				PreparedStatement ps = Library.con
						.prepareStatement("INSERT INTO book (callNumber, isbn, title, mainAuthor, publisher, year) VALUES (?,?,?,?,?,?)");
				ps.setString(1, callNumber);
				ps.setString(2, isbn);
				ps.setString(3, title);
				ps.setString(4, mainAuthor);
				ps.setString(5, publisher);
				ps.setInt(6, year);

				ps.executeUpdate();
				Library.con.commit();

				// Show Book table
				Statement stmt = Library.con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM Book");
				LibraryGUI.showTable(rs, "addBookButton");

				rs.close();
				ps.close();

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				for (int i = 0; i < otherAuthors.length; i++) {
					
					// Add other entered authors into hasAuthor table
					PreparedStatement ps = Library.con
							.prepareStatement("INSERT INTO hasAuthor VALUES (?,?)");
					ps.setString(1, callNumber);
					ps.setString(2, otherAuthors[i]);

					ps.executeUpdate();
					Library.con.commit();
					ps.close();
				}

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				for (int i = 0; i < subjects.length; i++) {
					
					// Add subjects into hasSubjects table
					PreparedStatement ps = Library.con
							.prepareStatement("INSERT INTO hasSubject VALUES (?,?)");
					ps.setString(1, callNumber);
					ps.setString(2, subjects[i]);

					ps.executeUpdate();
					Library.con.commit();
					ps.close();
				}

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void checkout() {
		// NEED TO THROW ERROR IF CALL NUMBER DOES NOT EXIST
		// NEED TO SET INDATE
		
		// User inputs: bid, list of call numbers
		JTextField bidField = new JTextField(10);
		JTextField callNumbersField = new JTextField(10);

		JComponent[] inputs = new JComponent[] {

		new JLabel("bid:"), bidField, new JLabel("Call number:"),
				callNumbersField,
		};
		int result = JOptionPane.showConfirmDialog(null, inputs,
				"Enter borrowing info", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			int bid = Integer.parseInt(bidField.getText());
			String[] callNumbers = callNumbersField.getText().split(",");

			try {

				java.util.Date currentDate = new java.util.Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date outDate = null;
				try {
					outDate = new Date(dateFormat.parse(
							dateFormat.format(currentDate)).getTime());
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				for (int i = 0; i < callNumbers.length; i++) {
					
					// Get all copies with the matching callNumber and status being in
					PreparedStatement ps2 = Library.con
							.prepareStatement("select * from bookcopy where callNumber = ? and status like 'in'");
					ps2.setString(1, callNumbers[i]);

					Date inDate = null;
					try {
						inDate = new Date(dateFormat.parse(
								dateFormat.format(currentDate)).getTime());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ps2.executeUpdate();
					ResultSet rs = ps2.getResultSet();
					
					if (rs.next() == true) {
						
						// Insert into Borrowing table if there exists a copy of the book with status "in"
						String copyNo = rs.getString("copyNo");
						PreparedStatement ps = Library.con
								.prepareStatement("INSERT INTO Borrowing (bid, callNumber, copyNo, outDate, inDate) VALUES (?,?,?,?,?)");
						ps.setInt(1, bid);
						ps.setString(2, callNumbers[i]);
						ps.setString(3, copyNo);
						ps.setDate(4, outDate);
						ps.setDate(5, inDate);

						ps.executeUpdate();
						Library.con.commit();

						// Set status of checked out copy to "out"
						PreparedStatement ps3 = Library.con
								.prepareStatement("update bookcopy set status = ? where callNumber = ? and copyNo=?");
						ps3.setString(1, "out");
						ps3.setString(2, callNumbers[i]);
						ps3.setString(3, copyNo);

						ps3.executeUpdate();
						Library.con.commit();

						ps.close();
						ps2.close();
						ps3.close();

					} else {
						// Error message for if there are no copies in
						new ErrorMessage("No copies are in for "
								+ callNumbers[i]);
					}
				}
				Statement stmt = Library.con.createStatement();
				ResultSet rs2 = stmt.executeQuery("SELECT * FROM Borrowing");
				LibraryGUI.showTable(rs2, "checkoutButton");
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void returnBooks(){
		// User inputs: callNumber, copyNo of book to be returned
				JTextField copyNoField = new JTextField(10);
				JTextField callNumbersField = new JTextField(10);

				JComponent[] inputs = new JComponent[] {

				 new JLabel("Call number:"), callNumbersField,new JLabel("Copy Number:"), copyNoField
				 
				};
				int result = JOptionPane.showConfirmDialog(null, inputs,
						"Enter borrowing info", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE);

				if (result == JOptionPane.OK_OPTION) {
					String copyNo = copyNoField.getText();
					String callNumbers = callNumbersField.getText();

					try {
						//get system current date
						java.util.Date currentDate = new java.util.Date();
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

							//Check if the Book was actually checked out and if it was then get due date for the book
							PreparedStatement ps2 = Library.con
									.prepareStatement("select Borrowing.inDate, Borrowing.bid, borrowing.borid from Borrowing INNER JOIN BookCopy on Borrowing.callNumber = BookCopy.callNumber and Borrowing.copyNo = BookCopy.copyNo where Borrowing.callNumber = ? and Borrowing.copyNo = ? and BookCopy.status like 'out'");
							ps2.setString(1, callNumbers);
							ps2.setString(2, copyNo);
							java.util.Date inDate = null;
							//Change system dates format
							try {
								currentDate = new Date(dateFormat.parse(
										dateFormat.format(currentDate)).getTime());
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							//execute above sql query
							ps2.executeUpdate();
							ResultSet rs = ps2.getResultSet();
							//If something was checked out
							if (rs.next() == true){
								//get date of the book
								inDate = rs.getDate("inDate");
								PreparedStatement ps = null;
								System.out.println(inDate);
								System.out.println(currentDate);
								//Compare the system date with inDate of teh book
//PEGGGGGYYYYY								//If inDate is before current date then issue a fine
								if (inDate.before(currentDate)){
									System.out.println("before");
									//Random amount of 100 for now
									ps = Library.con.prepareStatement("INSERT into FINE (amount, issuedDate, borid) values (?, ?, ?)");
									ps.setInt(1, 100);
									//change current date to sql date for Issue date of fine
									Date currentDateFine = null;
									try {
										currentDateFine = new Date(dateFormat.parse(
												dateFormat.format(currentDate)).getTime());
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									//get borid from the above result 
									ps.setDate(2, currentDateFine);
									ps.setInt(3, rs.getInt("borid"));
									ps.executeUpdate();
									Library.con.commit();
								}
								//Check if there is a hold request for the book
								//If so then change the status to on-hold else change to in
								//Also need to send an email but dont know how to do that yet
								PreparedStatement ps3 = Library.con
										.prepareStatement("select bid, issuedDate from HoldRequest where callNumber = ? ORDER BY issuedDate");
								ps3.setString(1, callNumbers);
								ps3.executeUpdate();
								ResultSet rs2 = ps3.getResultSet();
								PreparedStatement ps4 = null;
								//if there is a hold request then create the sql query accordingly
								if (rs2.next() == true){
									//Do sth for sending emails
									ps4 = Library.con.prepareStatement("update BookCopy Set status = 'on-hold' where callNumber = ? and copyNo = ?");
									ps4.setString(1, callNumbers);
									ps4.setString(2, copyNo);
									ps4.execute();
								}
								//If no hold request then change the status to in
								else{
									ps4 = Library.con.prepareStatement("update BookCopy Set status = 'in' where callNumber = ? and copyNo = ?");
									ps4.setString(1, callNumbers);
									ps4.setString(2, copyNo);
									ps4.execute();
									Library.con.commit();
								}
								Library.con.commit();
								ps2.close();
								ps3.close();
								ps4.close();
							}
							else {
								// Error message for if there are no copies in
								new ErrorMessage("This copy was not checked out");
							}
//PEGGGGGYYYYY							//PRINT TABLE DOES NOT WORK
//							Statement stmt = Library.con.createStatement();
//							ResultSet rs2 = stmt.executeQuery("SELECT * FROM Borrowing");
//							LibraryGUI.showTable(rs2, "processReturnButton");					
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
		
	}
	public void searchForBooks() {

	}

	public void addBookCopy() {
		// NEED TO CHECK IF CALLNUMBER IS IN BOOK TABLE

		// User inputs: callNumber
		JTextField callNumberField = new JTextField(15);

		JComponent[] inputs = new JComponent[] { new JLabel("Call number:"),
				callNumberField,

		};
		int result = JOptionPane.showConfirmDialog(null, inputs,
				"Enter book copy info", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			String callNumber = callNumberField.getText();
			try {
				// Get number of copies already in the database
				PreparedStatement ps = Library.con
						.prepareStatement("select * from bookcopy where callNumber = ?");

				ps.setString(1, callNumber);
				ps.executeUpdate();
				ResultSet rs = ps.getResultSet();

				List<String> copies = new ArrayList<String>();

				while (rs.next()) {
					copies.add(rs.getString("callNumber"));
				}

				// copyNo for the new copy
				String copyNum = Integer.toString(copies.size() + 1);

				// Add copy to BookCopy table 
				PreparedStatement ps2 = Library.con
						.prepareStatement("insert into BookCopy (callNumber, copyNo, status) VALUES (?,?,?)");
				ps2.setString(1, callNumber);
				ps2.setString(2, copyNum);
				ps2.setString(3, "in");

				ps2.executeUpdate();
				Library.con.commit();

				// Show BookCopy table
				Statement stmt = Library.con.createStatement();
				ResultSet rs2 = stmt.executeQuery("SELECT * FROM BookCopy");
				LibraryGUI.showTable(rs2, "addBookCopyButton");

				rs.close();
				rs2.close();
				ps.close();
				ps2.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
