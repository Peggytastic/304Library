
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class BorrowerTransactions {

	/*************************************************************************************
	*   BORROWER TRANSACTIONS:
	*   	- book search(TODO), check account, place holds(TODO), pay fine(TODO)
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
		System.out.println("Placing Holds");
	}
	
	public void payFine() {
		System.out.println("Paying fines");
	}
}
