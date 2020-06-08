package sharedPackage;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.GregorianCalendar;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import exportables.WeeklyProgram;

public abstract class SharedTableFrame extends JFrame
{
	private static final long serialVersionUID = 2795645238976127967L;

	protected enum OrderBy {TITLE, DATE, CINEMA_CODE, AVAIL_SEATS};
	
	protected final JPanel panel = new JPanel(new BorderLayout());
	
	protected JPanel buttonPanel = new JPanel(new GridLayout(18, 1));	
	protected JTable showTable;
	protected JScrollPane showPane;	
	
	protected WeeklyProgram program;
	
	public SharedTableFrame(String title, WeeklyProgram program)
	{
		if(title == null)
			throw new NullPointerException("Il titolo non può essere nullo.");
		if(program == null)
			throw new NullPointerException("Il programma settimanale non può essere nullo.");
		
		this.program = program;
		
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		panel.add(buttonPanel, BorderLayout.WEST);
		panel.add(new JLabel("      "), BorderLayout.CENTER);
		
		JPanel outerPanel = new JPanel(new BorderLayout());		
		outerPanel.add(new JLabel(" "), BorderLayout.NORTH);
 		outerPanel.add(new JLabel(" "), BorderLayout.SOUTH);
 		outerPanel.add(new JLabel("      "), BorderLayout.WEST);
 		outerPanel.add(new JLabel("      "), BorderLayout.EAST);
 		outerPanel.add(panel, BorderLayout.CENTER); 		
 		add(outerPanel);
 	}
	
	protected void display()
	{
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	protected static String getSimpleDate(GregorianCalendar date)
	{
		String first0 = date.get(GregorianCalendar.HOUR_OF_DAY) < 10 ? "0" : "";
		String second0 = date.get(GregorianCalendar.MINUTE) < 10 ? "0" : "";
		return date.get(GregorianCalendar.DAY_OF_MONTH) + " / " + (date.get(GregorianCalendar.MONTH) + 1) + " / " + date.get(GregorianCalendar.YEAR) +
				"  -  " + first0 + date.get(GregorianCalendar.HOUR_OF_DAY) + " : " + second0 + date.get(GregorianCalendar.MINUTE);
	}
}
