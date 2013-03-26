
import javax.swing.*;

import java.awt.*;


public class TablePane extends JPanel{
	
	public TablePane()
	{
		new FlowLayout(FlowLayout.RIGHT, 20, 10);
		
	}
	public void display() {
		JScrollPane scrollPane = new JScrollPane(this,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getRootPane().add(scrollPane);
		setVisible(true);
	}
}