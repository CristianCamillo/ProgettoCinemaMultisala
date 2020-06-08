package sharedPackage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import customerPackage.CustomerMainFrame;
import customerPackage.LoginPane;
import exportables.Cinema;
import exportables.Customer;
import exportables.Discount;
import exportables.WeeklyProgram;
import managerPackage.ManagerMainFrame;

public final class Launcher
{
	public final static String filePath = "DB.DAT";
	
	public static ArrayList<Customer> customers = new ArrayList<Customer>();
	public static ArrayList<Cinema> cinemas = new ArrayList<Cinema>();
	public static ArrayList<Discount> discounts = new ArrayList<Discount>();
	public static WeeklyProgram program = null;
	
	public static void main(String[] args)
	{		
		try
		{
			loadData();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Eccezione durante il caricamento del file. Chiusura...", "Errore", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		if(program == null || new GregorianCalendar().get(GregorianCalendar.WEEK_OF_YEAR) != program.getStartingDate().get(GregorianCalendar.WEEK_OF_YEAR))
			program = new WeeklyProgram();
		
		GregorianCalendar next12hours = new GregorianCalendar();
		next12hours.add(GregorianCalendar.HOUR_OF_DAY, 12);
		
		for(short i = 0; i < program.getNumberOfShows(); i++)
			if(next12hours.after(program.get(i).getDate()))
				program.get(i).getCinema().removeAllBooks();
		
		JFrame chooserFrame = new JFrame("Scegli tra cliente e gestore");
		chooserFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chooserFrame.setResizable(false);
		
		JPanel panel = new JPanel(new BorderLayout());
		JPanel innerPanel = new JPanel(new BorderLayout());
		JPanel gridPanel = new JPanel(new GridLayout(1, 2, 10, 10));
		
		JButton customerButton = new JButton("Cliente");
		JButton managerButton = new JButton("Gestore");
		customerButton.setPreferredSize(new Dimension(200, 150));
		
		gridPanel.add(customerButton);
		gridPanel.add(managerButton);
		
		innerPanel.add(new JLabel("Seleziona la modalità:"), BorderLayout.NORTH);
		innerPanel.add(new JLabel(" "), BorderLayout.CENTER);
		innerPanel.add(gridPanel, BorderLayout.SOUTH);
		
		panel.add(new JLabel(" "), BorderLayout.NORTH);
 		panel.add(new JLabel(" "), BorderLayout.SOUTH);
 		panel.add(new JLabel("      "), BorderLayout.WEST);
 		panel.add(new JLabel("      "), BorderLayout.EAST);
 		panel.add(innerPanel, BorderLayout.CENTER); 		
 				
		chooserFrame.add(panel);
		
		customerButton.addActionListener(new ActionListener()
		{ 
			public void actionPerformed(ActionEvent event)
			{
				Customer customer = LoginPane.getCustomer(customers);
				
				if(customer != null)
				{
					new CustomerMainFrame(customer, discounts, program).addWindowListener(new WindowListener()
					{
						public void windowClosing(WindowEvent e)
						{
							trySavingData();
						}
						
						public void windowClosed(WindowEvent e)
						{
							trySavingData();						
						}
						
						public void windowOpened(WindowEvent e){}
						public void windowIconified(WindowEvent e){}
						public void windowDeiconified(WindowEvent e){}
						public void windowActivated(WindowEvent e){}
						public void windowDeactivated(WindowEvent e){}					
					});
					chooserFrame.dispose();
				}
			} 
		});
		
		managerButton.addActionListener(new ActionListener()
		{ 
			public void actionPerformed(ActionEvent event)
			{				
				new ManagerMainFrame(cinemas, discounts, program).addWindowListener(new WindowListener()
				{
					public void windowClosing(WindowEvent e)
					{
						trySavingData();
					}
					
					public void windowClosed(WindowEvent e)
					{
						trySavingData();						
					}
					
					public void windowOpened(WindowEvent e){}
					public void windowIconified(WindowEvent e){}
					public void windowDeiconified(WindowEvent e){}
					public void windowActivated(WindowEvent e){}
					public void windowDeactivated(WindowEvent e){}					
				});
				chooserFrame.dispose();
			} 
		});
		
		chooserFrame.pack();
		chooserFrame.setLocationRelativeTo(null);
		chooserFrame.setVisible(true);
	}
	
	@SuppressWarnings("unchecked")
	public static void loadData() throws FileNotFoundException, IOException, ClassNotFoundException
	{
		File f = new File(filePath);
		
		if(f.exists())
		{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
			
			customers = (ArrayList<Customer>) in.readObject();
			cinemas = (ArrayList<Cinema>) in.readObject();
			discounts = (ArrayList<Discount>) in.readObject();
			program = (WeeklyProgram) in.readObject();
			
			in.close();
		}
	}
	
	public static void saveData() throws IOException
	{
		File f = new File(filePath);
		f.createNewFile();
		
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(customers);
		out.writeObject(cinemas);
		out.writeObject(discounts);
		out.writeObject(program);
		
		out.close();
	}
	
	public static void trySavingData()
	{
		try
		{
			saveData();
			System.exit(0);
		}
		catch(IOException e)
		{
			JOptionPane.showMessageDialog(null, "Eccezione durante il salvataggio del file. Chiusura...", "Errore", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
}
