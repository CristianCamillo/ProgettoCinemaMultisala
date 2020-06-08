package customerPackage;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import exceptions.RegistrationCanceledException;
import exportables.Category;
import exportables.Customer;

public final class RegisterPane
{
	public static void registerNewAccount(ArrayList<Customer> customers)
	{
		if(customers == null)
			throw new NullPointerException("La lista di clienti non può essere nulla.");

		JPanel panel = new JPanel(new BorderLayout());
		JPanel innerPanel = new JPanel(new BorderLayout(10, 10));
		JPanel westPanel = new JPanel(new GridLayout(3, 1, 0, 10));
		JPanel centerPanel = new JPanel(new GridLayout(3, 1, 0, 10));
		JPanel eastPanel = new JPanel(new BorderLayout());
		JPanel northEastPanel = new JPanel();
		
		JTextField addressField = new JTextField(20);
		JPasswordField passwordField = new JPasswordField(20);
		JPasswordField confirmField  = new JPasswordField(20);
		JComboBox<String> categoryBox = new JComboBox<String>(new String[] {"Adulto", "Bambino", "Studente", "Pensionato"});
		
		westPanel.add(new JLabel("Indirizzo email:"));
		westPanel.add(new JLabel("Password:"));
		westPanel.add(new JLabel("Conferma password:"));		
		centerPanel.add(addressField);
		centerPanel.add(passwordField);
		centerPanel.add(confirmField);		
		northEastPanel.add(new JLabel("Categoria:"));
		northEastPanel.add(categoryBox);
		eastPanel.add(northEastPanel, BorderLayout.NORTH);
		
		innerPanel.add(westPanel, BorderLayout.WEST);
		innerPanel.add(centerPanel, BorderLayout.CENTER);
		innerPanel.add(eastPanel, BorderLayout.EAST);
		
		panel.add(new JLabel(" "), BorderLayout.NORTH);
		panel.add(new JLabel(" "), BorderLayout.SOUTH);
		panel.add(new JLabel("    "), BorderLayout.WEST);
		panel.add(new JLabel("    "), BorderLayout.EAST);
		panel.add(innerPanel, BorderLayout.CENTER);		
 		
 		while(true)
			switch(JOptionPane.showOptionDialog(null, panel, "Registra", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[] {"Registra"}, null))
			{
				case 0:
					String address = addressField.getText();
					String password = String.valueOf(passwordField.getPassword());
					String confirm = String.valueOf(confirmField.getPassword());
					
					if(address.isEmpty() || password.isEmpty() || confirm.isEmpty())
					{
						JOptionPane.showMessageDialog(null, "Non tutti i campi sono stati inseriti.", "Errore", JOptionPane.ERROR_MESSAGE);
						break;
					}
					if(!password.equals(confirm))
					{
						JOptionPane.showMessageDialog(null, "Password non corrispondenti.", "Errore", JOptionPane.ERROR_MESSAGE);
						break;
					}
					

					Category category;
					
					switch(categoryBox.getSelectedIndex())
					{
						case 0:
							category = Category.ADULT;
							break;
						case 1:
							category = Category.CHILD;
							break;
						case 2:
							category = Category.STUDENT;
							break;
						case 3:
							category = Category.RETIRED;
							break;
						default: category = null;
					}
					
					Customer newCustomer;
					
					try
					{
						newCustomer = new Customer(address, password, category);
					}
 					catch(IllegalArgumentException e)
					{
						JOptionPane.showMessageDialog(null, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
						break;
					}					
					
					int index = customers.indexOf(newCustomer);
					
					if(index == -1)
					{
						customers.add(newCustomer);
						return;
					}
					else
						JOptionPane.showMessageDialog(null, "Account già registrato.", "Errore", JOptionPane.ERROR_MESSAGE);								
					break;
				default:
					throw new RegistrationCanceledException();
			}
	}
}
