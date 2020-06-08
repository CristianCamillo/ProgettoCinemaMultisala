package managerPackage;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import exportables.Cinema;

public final class CreateCinemaPane
{		
	public static Cinema create(ArrayList<Cinema> cinemas)
	{
		if(cinemas == null)
			throw new NullPointerException("La lista delle sale cinematografiche può essere nulla.");
		
		JComboBox<String> widthBox;
		JComboBox<String> depthBox;
		
		String[] array = new String[Cinema.MAX_WIDTH];
		for(byte i = 0; i < array.length; i++)
			array[i] = i + 1 + "";
		widthBox = new JComboBox<String>(array);
		
		array = new String[Cinema.MAX_DEPTH];
		for(byte i = 0; i < array.length; i++)
			array[i] = i + 1 + "";
		depthBox = new JComboBox<String>(array);
		
		JPanel panel = new JPanel(new BorderLayout());
		JPanel westPanel = new JPanel(new GridLayout(2, 1));
		JPanel eastPanel = new JPanel(new GridLayout(2, 1));
		JPanel seatsPanel = new JPanel();
		
		JTextField codeField = new JTextField(3);
		
		seatsPanel.add(widthBox);
		seatsPanel.add(new JLabel("x"));
		seatsPanel.add(depthBox);
		
		westPanel.add(new JLabel("Codice:"));
		westPanel.add(new JLabel("Posti:"));

		eastPanel.add(codeField);
		eastPanel.add(seatsPanel);
		
		panel.add(westPanel, BorderLayout.WEST);
		panel.add(eastPanel, BorderLayout.EAST);
		
		codeField.setText(cinemas.size() + 1 + "");
		codeField.setEditable(false);		
		
		if(JOptionPane.showOptionDialog(null, panel, "Aggiungi sala cinema", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,	null, new String[] {"Ok", "Annulla"}, null) == 0)
			return new Cinema((byte)(cinemas.size() + 1), (byte)(widthBox.getSelectedIndex() + 1),  (byte)(depthBox.getSelectedIndex() + 1));
		else
			return null;
	}
}
