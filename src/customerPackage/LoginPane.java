package customerPackage;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import exceptions.RegistrationCanceledException;
import exportables.Customer;

public final class LoginPane
{
	public static Customer getCustomer(ArrayList<Customer> customers)
	{	
		if(customers == null)
			throw new NullPointerException("La lista di clienti non può essere nulla.");
		
		JPanel panel = new JPanel(new BorderLayout());
		JPanel innerPanel = new JPanel(new BorderLayout(10, 10));
		JPanel westPanel = new JPanel(new GridLayout(2, 1, 0, 10));
		JPanel eastPanel = new JPanel(new GridLayout(2, 1, 0, 10));
		
		JTextField addressField = new JTextField(20);
		JPasswordField passwordField = new JPasswordField(20);
		
		passwordField.setEchoChar('*');
		
		westPanel.add(new JLabel("Email:"));
		westPanel.add(new JLabel("Password:"));		
		eastPanel.add(addressField);
		eastPanel.add(passwordField);		
		innerPanel.add(westPanel, BorderLayout.WEST);
		innerPanel.add(eastPanel, BorderLayout.EAST);
		
		panel.add(new JLabel(" "), BorderLayout.NORTH);
		panel.add(new JLabel(" "), BorderLayout.SOUTH);
		panel.add(new JLabel("    "), BorderLayout.WEST);
		panel.add(new JLabel("    "), BorderLayout.EAST);
		panel.add(innerPanel, BorderLayout.CENTER);		
 		
 		while(true)
			switch(JOptionPane.showOptionDialog(null, panel, "Login", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[] {"Autentica", "Registra", "Annulla"}, null))
			{
				case 0:
					String address = addressField.getText();
					String password = String.valueOf(passwordField.getPassword());
					
					try
					{
						int index = customers.indexOf(new Customer(address, password, null));
						if(index != -1)
							return customers.get(index);
						else
							JOptionPane.showMessageDialog(null, "Credenziali incorrette.", "Errore", JOptionPane.ERROR_MESSAGE);
					}
					catch(IllegalArgumentException e)
					{
						JOptionPane.showMessageDialog(null, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
					}										
					break;
				case 1:
					try
					{
						RegisterPane.registerNewAccount(customers);
					}
					catch(RegistrationCanceledException e){}
					break;
				default:
					return null;
			}
	}
}