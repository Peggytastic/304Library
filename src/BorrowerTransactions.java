
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class BorrowerTransactions {

	/*************************************************************************************
	*   BORROWER TRANSACTIONS:
	*   	- book search(TODO), check account, place holds, pay fine(TODO)
	*************************************************************************************/
	public void searchForBooks() {
		// User inputs: title, author
		JTextField titleField = new JTextField(30);
		JTextField authorField = new JTextField(30);

		JComponent[] inputs = new JComponent[] { 
				new JLabel("Title:"), titleField,
				new JLabel("Author:"), authorField, };

		Object[] options = { "Search", "Cancel" };
		int result = JOptionPane.showOptionDialog(null, inputs, "Book Search",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[0]);

	}

	public void checkAccount() {

		// User inputs: bid, password
		JTextField bidField = new JTextField(15);
		JTextField passwordField = new JTextField(15);

		JComponent[] inputs = new JComponent[] { 
				new JLabel("bid:"), bidField,
				new JLabel("password:"), passwordField,

		};
		int result = JOptionPane.showConfirmDialog(null, inputs,
				"Enter borrower info", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			int bid = Integer.parseInt(bidField.getText());

			String password = passwordField.getText();

			try {
				PreparedStatement ps = Library.con
						.prepareStatement("select * from borrower where bid = ?");
				ps.setInt(1, bid);
				ps.executeQuery();

				ResultSet rs = ps.getResultSet();
				rs.next();

				// Check if password is correct
				if (!rs.getString("password").equals(password)) {
					new ErrorMessage("Incorrect password!");
				}

				else {

					// Select items the borrower has currently borrowed and not
					// yet returned
					PreparedStatement ps2 = Library.con
							.prepareStatement("select borrowing.borid, bookcopy.callNumber, bookcopy.copyNo, borrowing.outDate, borrowing.inDate from Borrowing, BookCopy where Borrowing.callNumber=BookCopy.callNumber and Borrowing.copyNo=BookCopy.CopyNo and BookCopy.Status = 'out' and Borrowing.bid = ?");
					ps2.setInt(1, bid);
					ps2.executeQuery();

					// Select outstanding fines
					PreparedStatement ps3 = Library.con
							.prepareStatement("Select fid, amount, issuedDate from Fine WHERE paidDate is NULL and borid in (select borrowing.borid from Borrowing, BookCopy where Borrowing.callNumber = BookCopy.callNumber and Borrowing.copyNo = BookCopy.copyNo and Borrowing.bid = ?)");
					ps3.setInt(1, bid);
					ps3.executeQuery();

					// Select hold requests
					PreparedStatement ps4 = Library.con
							.prepareStatement("select holdrequest.hid, holdrequest.issuedDate, Book.callNumber, Book.isbn, Book.title from Book INNER JOIN HoldRequest on Book.callNumber = HoldRequest.callNumber where HoldRequest.bid = ?");
					ps4.setInt(1, bid);
					ps4.executeQuery();

					// Show tables
					LibraryGUI.showAccountTables(ps2.getResultSet(),
							ps3.getResultSet(), ps4.getResultSet(), bid);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void placeHold() {
		// User inputs: bid, password
				JTextField bidField = new JTextField(15);
				JTextField passwordField = new JTextField(15);
				JTextField callNumberField = new JTextField(15);

				JComponent[] inputs = new JComponent[] { 
						new JLabel("bid:"), bidField,
						new JLabel("password:"), passwordField,
						new JLabel("call number:"), callNumberField,

				};
				int result = JOptionPane.showConfirmDialog(null, inputs,
						"Enter information", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE);

				if (result == JOptionPane.OK_OPTION) {
					int bid = Integer.parseInt(bidField.getText());
					String password = passwordField.getText();
					String callNumber = callNumberField.getText();

					try {
						PreparedStatement ps = Library.con
								.prepareStatement("select * from borrower where bid = ?");
						ps.setInt(1, bid);
						ps.executeQuery();

						ResultSet rs = ps.getResultSet();
						rs.next();

						// Check if password is correct
						if (!rs.getString("password").equals(password)) {
							new ErrorMessage("Incorrect password!");
						}

						else {

							// Check if there is a copy of the book currently in
							PreparedStatement ps2 = Library.con
									.prepareStatement("select callNumber from bookCopy where status like 'in' and callNumber=?");
							ps2.setString(1, callNumber);
							ps2.executeQuery();
							
							PreparedStatement ps4 = Library.con
									.prepareStatement("select callNumber from bookCopy where status like 'out' and callNumber=?");
							ps4.setString(1, callNumber);
							ps4.executeQuery();
							
							// If there is a copy of the book that is in or no copies that are out, error message
							if (ps2.getResultSet().next() != false){
								new ErrorMessage("You cannot place this book on hold because there are copies that are in");
							}
							
							else if(ps4.getResultSet().next() == false){
								 new ErrorMessage("There are no copies avaiable to be put on hold");
							}
							
							// If there are no copies of the book in, place a hold for the book
							else{
								
								java.util.Date currentDate = new java.util.Date();
								DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
								Date issuedDate = null;
								try {
									issuedDate = new Date(dateFormat.parse(
											dateFormat.format(currentDate)).getTime());
								} catch (ParseException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}
								
							PreparedStatement ps3 = Library.con
									.prepareStatement("insert into HoldRequest (bid, callNumber, issuedDate) VALUES (?,?,?)");
							ps3.setInt(1, bid);
							ps3.setString(2, callNumber);
							ps3.setDate(3, issuedDate);
							ps3.executeUpdate();
							Library.con.commit();

							// Show table
							Statement stmt = Library.con.createStatement();
							ResultSet rs2 = stmt.executeQuery("SELECT * FROM HoldRequest");
							LibraryGUI.showTable(rs2, "holdRequestButton");
							
							ps.close();
							ps2.close();
							ps3.close();
							rs.close();
							rs2.close();

							
							}
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	}
	
	public void payFine() {
		System.out.println("Paying fines");
	}
}
