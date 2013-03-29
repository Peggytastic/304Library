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
import java.util.Calendar;
import java.util.List;
import java.util.Random;

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
			
			final ClerkTransactions clerk = new ClerkTransactions();

			addBorrowerButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clerk.addBorrower();
				}
			});

			checkoutButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clerk.checkout();
				}
			});

			processReturnButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clerk.returnBooks();
				}
			});
			
			checkOverdueButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clerk.checkOverdueItems();
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
			
			final BorrowerTransactions borrower = new BorrowerTransactions();

			this.add(searchButton);
			this.add(checkAccountButton);
			this.add(placeHoldButton);
			this.add(payFineButton);

			searchButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					borrower.searchForBooks();
				}
			});
			
			checkAccountButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					borrower.checkAccount();
				}
			});
			
			placeHoldButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					borrower.placeHold();
				}
			});
			
			payFineButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					borrower.payFine();
				}
			});
		}

		if (user == "Librarian") {

			JButton addBookButton = new JButton("Add book");
			JButton addBookCopyButton = new JButton("Add book copy");
			JButton genReportCheckoutButton = new JButton("Generate report for all checked out books");
			JButton genReportPopularButton = new JButton("Generate report for popular books");
			
			final LibrarianTransactions librarian = new LibrarianTransactions();

			addBookButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					librarian.addBook();
				}
			});

			addBookCopyButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					librarian.addBookCopy();
				}
			});
			
			genReportCheckoutButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					librarian.generateCheckedOutBooksReport();
				}
			});
			
			genReportPopularButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					librarian.generatePopularBooksReport();
				}
			});
			
			this.add(addBookButton);
			this.add(addBookCopyButton);
			this.add(genReportCheckoutButton);
			this.add(genReportPopularButton);
		}
	}
}