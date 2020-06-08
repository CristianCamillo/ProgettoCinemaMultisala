package managerPackage;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import exportables.Category;
import exportables.Discount;

public final class CreateDiscountPane
{
	public static Discount create(ArrayList<Discount> discounts)
	{
		if(discounts == null)
			throw new NullPointerException("La lista delle politiche di sconto non può essere nulla.");
		
		String[] percentages = new String[101];
		for(byte i = 0; i <= 100; i++)
			percentages[i] = i + "";
		
		BorderLayout bl = new BorderLayout();
		bl.setHgap(10);
		
		String[] options = new String[] {
				"Fascia mattutina", "Fascia pomeridiana", "Adulto", "Bambino", "Studente", "Pensionato", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica"};
		
		JPanel panel = new JPanel(bl);
		JPanel percPanel = new JPanel();
		JComboBox<String> optionBox = new JComboBox<String>(options);
		JComboBox<String> percBox = new JComboBox<String>(percentages);
		
		percPanel.add(percBox);
		percPanel.add(new JLabel(" %"));
		
		panel.add(new JLabel("Seleziona un parametro:"), BorderLayout.WEST);
		panel.add(optionBox, BorderLayout.CENTER);
		panel.add(percPanel, BorderLayout.EAST);
		
		if(JOptionPane.showOptionDialog(null, panel, "Aggiungi politica di sconto", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,	null, new String[] {"Ok", "Annulla"}, null) == 0)
		{
			Category category;
			
			if(optionBox.getSelectedIndex() == 0)
				category = Category.MORNING;
			else if(optionBox.getSelectedIndex() == 1)
				category = Category.AFTERNOON;
			else if(optionBox.getSelectedIndex() == 2)
				category = Category.ADULT;
			else if(optionBox.getSelectedIndex() == 3)
				category = Category.CHILD;
			else if(optionBox.getSelectedIndex() == 4)
				category = Category.STUDENT;
			else if(optionBox.getSelectedIndex() == 5)
				category = Category.RETIRED;
			else if(optionBox.getSelectedIndex() == 6)
				category = Category.MONDAY;
			else if(optionBox.getSelectedIndex() == 7)
				category = Category.TUESDAY;
			else if(optionBox.getSelectedIndex() == 8)
				category = Category.WEDNESDAY;
			else if(optionBox.getSelectedIndex() == 9)
				category = Category.THURSDAY;
			else if(optionBox.getSelectedIndex() == 10)
				category = Category.FRIDAY;
			else if(optionBox.getSelectedIndex() == 11)
				category = Category.SATURDAY;
			else
				category = Category.SUNDAY;
			
			Discount discount = new Discount(category, percBox.getSelectedIndex());
			
			if(discounts.indexOf(discount) == -1)
				return discount;
			else
				JOptionPane.showMessageDialog(null, "Politica di sconto simile già presente.", "Errore", JOptionPane.ERROR_MESSAGE);
		}
		
		return null;
	}
}
