import java.awt.BorderLayout;
import java.awt.Color;
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

import javax.swing.*;

public class ActivitiesPane extends JPanel { // The info panel for when photos
												// are displayed (panel on the
												// right side)
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

					JTextField passwordField = new JTextField(10);
					JTextField nameField = new JTextField(10);
					JTextField addressField = new JTextField(10);
					JTextField phoneField = new JTextField(10);
					JTextField emailAddressField = new JTextField(10);
					JTextField sinOrStNoField = new JTextField(10);
					JTextField typeField = new JTextField(10);
					JTextField expiryDateField = new JTextField(20);

					JComponent[] inputs = new JComponent[] {

					new JLabel("Password:"), passwordField,
							new JLabel("Name:"), nameField,
							new JLabel("Address:"), addressField,
							new JLabel("Phone:"), phoneField,
							new JLabel("Email:"), emailAddressField,
							new JLabel("Sin or St No.:"), sinOrStNoField,
							new JLabel("Type:"), typeField,
							new JLabel("Expiry date:"), expiryDateField,

					};
					int result = JOptionPane.showConfirmDialog(null, inputs,
							"Enter borrower info",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE);

					if (result == JOptionPane.OK_OPTION) {

						String password = passwordField.getText();
						String name = nameField.getText();
						String address = addressField.getText();
						int phone = Integer.parseInt(phoneField.getText());
						String emailAddress = emailAddressField.getText();
						int sinOrStNo = Integer.parseInt(sinOrStNoField
								.getText());
						String type = typeField.getText();
						Date expiryDate = null;

						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						String date = expiryDateField.getText();

						try {
							expiryDate = new Date(df.parse(date).getTime());
						} catch (ParseException ex) {
							ex.printStackTrace();
						}

						PreparedStatement ps;

						try {
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

							Statement stmt = Library.con.createStatement();
							ResultSet rs = stmt
									.executeQuery("SELECT * FROM Borrower");
							LibraryGUI.tablePane.removeAll();
							LibraryGUI.tablePane.updateUI();
							LibraryGUI.showTable(rs, null, "addBorrowerButton");

							rs.close();

							ps.close();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				}

			});

			checkoutButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JTextField bidField = new JTextField(10);
					JTextField callNumberField = new JTextField(10);

					JComponent[] inputs = new JComponent[] {

					new JLabel("bid:"), bidField, new JLabel("Call number:"),
							callNumberField,

					};
					int result = JOptionPane.showConfirmDialog(null, inputs,
							"Enter borrowing info",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE);
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
			JButton genReportCheckoutButton = new JButton(
					"Generate report for all checked out books");
			JButton genReportPopularButton = new JButton(
					"Generate report for popular books");

			addBookButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					JTextField callNumberField = new JTextField(15);
					JTextField isbnField = new JTextField(10);
					JTextField titleField = new JTextField(10);
					JTextField mainAuthorField = new JTextField(10);
					JTextField otherAuthorsField = new JTextField(100);
					JTextField publisherField = new JTextField(10);
					JTextField yearField = new JTextField(10);
					JTextField subjectsField = new JTextField(100);

					JComponent[] inputs = new JComponent[] {

					new JLabel("Call number:"), callNumberField,
							new JLabel("isbn:"), isbnField,
							new JLabel("Title:"), titleField,
							new JLabel("Main author:"), mainAuthorField,
							new JLabel("Other authors:"), otherAuthorsField,
							new JLabel("Publisher:"), publisherField,
							new JLabel("Year:"), yearField,
							new JLabel("Subjects:"), subjectsField

					};
					int result = JOptionPane.showConfirmDialog(null, inputs,
							"Enter book info", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE);

					if (result == JOptionPane.OK_OPTION) {
						String callNumber = callNumberField.getText();
						String isbn = isbnField.getText();
						String title = titleField.getText();
						String mainAuthor = mainAuthorField.getText();
						String otherAuthors = otherAuthorsField.getText();
						String publisher = publisherField.getText();
						int year = Integer.parseInt(yearField.getText());
						String subjects = subjectsField.getText();

						String[] otherAuthorsArray = otherAuthors.split(",");
						String[] subjectsArray = subjects.split(",");

						try {
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

							Statement stmt = Library.con.createStatement();
							ResultSet rs = stmt
									.executeQuery("SELECT * FROM Book");
							LibraryGUI.tablePane.removeAll();
							LibraryGUI.tablePane.updateUI();
							LibraryGUI.showTable(rs, null, "addBookButton");

							rs.close();

							ps.close();

						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						try {
							for (int i = 0; i < otherAuthorsArray.length; i++) {
								PreparedStatement ps = Library.con
										.prepareStatement("INSERT INTO hasAuthor VALUES (?,?)");
								ps.setString(1, callNumber);
								ps.setString(2, otherAuthorsArray[i]);

								ps.executeUpdate();
								Library.con.commit();
								ps.close();
							}

						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						try {
							for (int i = 0; i < subjectsArray.length; i++) {
								PreparedStatement ps = Library.con
										.prepareStatement("INSERT INTO hasSubject VALUES (?,?)");
								ps.setString(1, callNumber);
								ps.setString(2, subjectsArray[i]);

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
			});
			this.add(addBookButton);
			this.add(genReportCheckoutButton);
			this.add(genReportPopularButton);

		}

	}
}
