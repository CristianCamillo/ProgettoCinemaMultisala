package sharedPackage;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import exceptions.SeatStateNotChangedException;
import exportables.Cinema;
import exportables.Customer;
import exportables.Discount;
import exportables.Show;
import exportables.WeeklyProgram;

public final class ShowPane
{
	protected final static JLabel titleLabel = new JLabel("Titolo:");
	protected final static JLabel priceLabel = new JLabel("Prezzo:");
	protected final static JLabel dateLabel = new JLabel("Data:");
	protected final static JLabel runningTimeLabel = new JLabel("Durata:");
	protected final static JLabel cinemaLabel = new JLabel("N.Sala:");
	protected final static JLabel descriptionLabel = new JLabel("Descrizione:");
	private final static JLabel euroLabel = new JLabel("€");
	private final static JLabel minuteLabel = new JLabel("minuti");
	
	protected final JTextField titleField = new JTextField();
	protected final JTextField priceField = new JTextField();
	protected final JTextField dateField = new JTextField();
	protected final JTextField runningTimeField = new JTextField();
	protected final JTextField cinemaField = new JTextField();
	
	protected final JTextArea descriptionArea = new JTextArea();
	protected final JScrollPane descriptionPane = new JScrollPane(descriptionArea);
	
	protected JPanel panel = new JPanel(null);
	
	private Show show;
	
	public ShowPane(Show show)
	{
		if(show == null)
			throw new IllegalArgumentException("Lo spettacolo non può essere nullo.");
		
		this.show = show;
		
		int panelWidth = 400;
		int panelHeight = 400;
		int componentHeight = 25;
			
		panel.setPreferredSize(new Dimension(panelWidth, panelHeight));
		
		int labelWidth = 45;		
		titleLabel.      setBounds(0,   			 40 * 0,	  labelWidth, componentHeight);
		priceLabel.      setBounds(0,  	  			 40 * 1,	  labelWidth, componentHeight);
		dateLabel.       setBounds(0,   			 40 * 2,	  labelWidth, componentHeight);
		runningTimeLabel.setBounds(0,   			 40 * 3,	  labelWidth, componentHeight);
		cinemaLabel.     setBounds(0,   			 40 * 4, 	  labelWidth, componentHeight);
		descriptionLabel.setBounds(0,  			     40 * 5 + 10, 80,         componentHeight);
		euroLabel.       setBounds(panelWidth - 17,  40 * 1,      25, 	  	  componentHeight);
		minuteLabel.	 setBounds(labelWidth + 45,  40 * 3,      labelWidth, componentHeight);
		
		int fieldWidth = panelWidth - labelWidth - 10;		
		titleField.		 setBounds(labelWidth + 10, 40 * 0, fieldWidth, 	 componentHeight);
		priceField.		 setBounds(labelWidth + 10, 40 * 1, fieldWidth - 25, componentHeight);
		dateField.		 setBounds(labelWidth + 10, 40 * 2, 140,      		 componentHeight);
		runningTimeField.setBounds(labelWidth + 10, 40 * 3, 30,              componentHeight);
		cinemaField.     setBounds(labelWidth + 10, 40 * 4, 30,              componentHeight);
		
		descriptionPane.setBounds(0, 235, panelWidth, 165);
		
		panel.add(titleLabel);
		panel.add(priceLabel);
		panel.add(dateLabel);
		panel.add(runningTimeLabel);
		panel.add(cinemaLabel);
		panel.add(descriptionLabel);
		panel.add(euroLabel);
		panel.add(minuteLabel);
		
		panel.add(titleField);
		panel.add(priceField);
		panel.add(dateField);
		panel.add(runningTimeField);
		panel.add(cinemaField);
		
		panel.add(descriptionPane);
		
		titleField.setFocusable(false);
		priceField.setFocusable(false);
		dateField.setFocusable(false);
		runningTimeField.setFocusable(false);
		cinemaField.setFocusable(false);		
		descriptionArea.setFocusable(false);
		
		titleField.setText(show.getTitle());
		priceField.setText(show.getPrice() + "");
		dateField.setText(getSimpleDate(show.getDate()));
		runningTimeField.setText(show.getRunningTime() + "");
		cinemaField.setText(show.getCinema().getCode() + "");
		descriptionArea.setText(show.getDescription());
	}
	
	
	public void showManagerPane()
	{
		byte result = (byte)JOptionPane.showOptionDialog(null, panel, "Spettacolo", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				new String[] {"Ok", "Modifica posti in sala"}, null);
		
		if(result == 1)
		{	
			if(new GregorianCalendar().compareTo(show.getDate()) < 0)
			{
				Cinema newCinema = CinemaGraphicsPane.getModified(show.getCinema().clone(), null);
				if(newCinema != null)
					show.setCinema(newCinema);
			}
			else if(result == 1)
				JOptionPane.showMessageDialog(null, "Lo spettacolo \""+ show.getTitle() + "\" è stato già eseguito.", "Errore", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void showCustomerPane(Customer customer, ArrayList<Discount> discounts, WeeklyProgram program)
	{
		byte result;
		
		if(customer == null)
			throw new NullPointerException("Il cliente non può essere nullo.");
		if(discounts == null)
			throw new NullPointerException("La lista delle politiche di sconto non può essere nulla.");
		if(program == null)
			throw new NullPointerException("Il programma settimanale non può essere nullo.");
		
		do
		{
			result = (byte)JOptionPane.showOptionDialog(null, panel, "Spettacolo: " + show.getTitle(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
					new String[] {"Prenota\\Aquista un posto", "Annulla prenotazione"}, null);
			
			if(result != -1)
			{	
				if(new GregorianCalendar().compareTo(show.getDate()) < 0)
				{
					if(result == 0)
					{						
						if(!show.getCinema().isSoldTo(customer))
						{
							byte state = 0;
							
							if(show.getCinema().isBookedBy(customer))
								state = 1;
								
							Cinema newCinema = CinemaGraphicsPane.getModified(show.getCinema().clone(), customer);
							
							if(newCinema != null)
								if(state == 0 && newCinema.isBookedBy(customer))
								{
									GregorianCalendar next12hours = new GregorianCalendar();
									next12hours.add(GregorianCalendar.HOUR_OF_DAY, 12);
									if(next12hours.after(show.getDate()))
										JOptionPane.showMessageDialog(null, "Impossibile prenotare a meno di 12 dall'inizio dello spettacolo.", "Errore", JOptionPane.ERROR_MESSAGE);
									else if(JOptionPane.showOptionDialog(null, "Vuoi prenotare questo posto?", show.getTitle(),
											JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] {"Si", "No"}, null) == 0)
										show.setCinema(newCinema);
								}
								else if(newCinema.isSoldTo(customer))
								{
									float price = show.getPrice();
									
									if(program.discountEnabled())
										for(int i = 0; i < discounts.size(); i++)
										{
											float newPrice = discounts.get(i).generatePrice(show, customer);
											if(newPrice < price)
												price = newPrice;
										}
									
									if(JOptionPane.showOptionDialog(null, "Vuoi acquistare un biglietto per questo posto? Ti costerà: " + price + " €.", show.getTitle(),
											JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] {"Si", "No"}, null) == 0)
									{
										show.setCinema(newCinema);
										program.registerEarning(show, price);
									}
								}
						}
						else
						{
							for(byte y = 0; y < show.getCinema().getDepth(); y++)
								for(byte x = 0; x < show.getCinema().getWidth(); x++)
									if(show.getCinema().isSeatSoldTo(x, y, customer))
									{
										JOptionPane.showMessageDialog(null, "E' stato già acquistato un biglietto per questo spettacolo. (posto " + (x + 1) + "-" + (y + 1) + ")", "Errore", JOptionPane.ERROR_MESSAGE);
										break;
									}
						}
					}
					else
					{
						try
						{
							show.getCinema().removeBook(customer);
							JOptionPane.showMessageDialog(null, "Prenotazione annullata con successo.", "Informazione", JOptionPane.INFORMATION_MESSAGE);
						}
						catch(SeatStateNotChangedException e)
						{
							JOptionPane.showMessageDialog(null, "Non è stata trovata alcuna prenotazione.", "Errore", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				else
					JOptionPane.showMessageDialog(null, "Lo spettacolo \""+ show.getTitle() + "\" non è più disponibile", "Errore", JOptionPane.ERROR_MESSAGE);
			}
		}
		while(result != -1);
	}
	
	private static String getSimpleDate(GregorianCalendar date)
	{
		String first0 = date.get(GregorianCalendar.HOUR_OF_DAY) < 10 ? "0" : "";
		String second0 = date.get(GregorianCalendar.MINUTE) < 10 ? "0" : "";
		return date.get(GregorianCalendar.DAY_OF_MONTH) + " / " + (date.get(GregorianCalendar.MONTH) + 1) + " / " + date.get(GregorianCalendar.YEAR) +
				"  -  " + first0 + date.get(GregorianCalendar.HOUR_OF_DAY) + " : " + second0 + date.get(GregorianCalendar.MINUTE);	
	}
}
