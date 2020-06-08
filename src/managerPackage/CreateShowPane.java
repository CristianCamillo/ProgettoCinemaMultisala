package managerPackage;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import exportables.Cinema;
import exportables.Show;

public final class CreateShowPane
{
	private final static JTextField titleField = new JTextField();
	private final static JTextField priceField = new JTextField();
	
	private final static JComboBox<String> dateBox = new JComboBox<String>(new String[] {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica"});
	private static JComboBox<String> hourBox;
	private static JComboBox<String> minuteBox;
	private static JComboBox<String> runningMinuteBox;
	
	private static JComboBox<String> cinemaBox;
	
	private final static JTextArea descriptionArea = new JTextArea();
	
	public static Show create(ArrayList<Cinema> cinemas)
	{	
		if(cinemas == null)
			throw new NullPointerException("La lista delle sale cinematografiche può essere nulla.");
		if(cinemas.size() == 0)
			throw new IllegalArgumentException("La lista delle sale cinematografiche può essere vuota.");
		
		JLabel titleLabel = new JLabel("Titolo:");
		JLabel priceLabel = new JLabel("Prezzo:");
		JLabel dateLabel = new JLabel("Data:");
		JLabel runningTimeLabel = new JLabel("Durata:");
		JLabel cinemaLabel = new JLabel("N.Sala:");
		JLabel descriptionLabel = new JLabel("Descrizione:");
		JLabel euroLabel = new JLabel("€");
		JLabel lineLabel = new JLabel("-");
		JLabel dotsLabel = new JLabel(":");
		JLabel minuteLabel = new JLabel("minuti");		
		
		JScrollPane descriptionPane = new JScrollPane(descriptionArea);
		
		String[] numbers = new String[24];
		for(short i = 0; i < 24; i++)
			numbers[i] = i + "";
		hourBox = new JComboBox<String>(numbers);
		
		numbers = new String[60];
		for(short i = 0; i < 60; i++)
			numbers[i] = i + "";
		minuteBox = new JComboBox<String>(numbers);
		
		numbers = new String[300];
		for(short i = 1; i <= 300; i++)
			numbers[i - 1] = i + "";
		runningMinuteBox = new JComboBox<String>(numbers);
		
		String[] cinemaCodes = new String[cinemas.size()];
		for(byte i = 0; i < cinemas.size(); i++)
			cinemaCodes[i] = "Sala " + (i + 1);
		cinemaBox = new JComboBox<String>(cinemaCodes);
		
		int panelWidth = 400;
		int panelHeight = 400;
		int componentHeight = 25;
		
		JPanel panel = new JPanel(null);		
		panel.setPreferredSize(new Dimension(panelWidth, panelHeight));
		
		int labelWidth = 45;		
		titleLabel.      setBounds(0,   			 40 * 0,	  labelWidth, componentHeight);
		priceLabel.      setBounds(0,  	  			 40 * 1,	  labelWidth, componentHeight);
		dateLabel.       setBounds(0,   			 40 * 2,	  labelWidth, componentHeight);
		runningTimeLabel.setBounds(0,   			 40 * 3,	  labelWidth, componentHeight);
		cinemaLabel.     setBounds(0,   			 40 * 4, 	  labelWidth, componentHeight);
		descriptionLabel.setBounds(0,  			     40 * 5 + 10, 80,         componentHeight);
		euroLabel.       setBounds(panelWidth - 17,  40 * 1,      25, 	  	  componentHeight);
		lineLabel.       setBounds(labelWidth + 110, 40 * 2,      25, 	  	  componentHeight);
		dotsLabel.		 setBounds(labelWidth + 170, 40 * 2,      25,         componentHeight);
		minuteLabel.	 setBounds(labelWidth + 65,  40 * 3,      labelWidth, componentHeight);
		
		int fieldWidth = panelWidth - labelWidth - 10;		
		titleField.setBounds(labelWidth + 10, 40 * 0, fieldWidth, 	   componentHeight);
		priceField.setBounds(labelWidth + 10, 40 * 1, fieldWidth - 25, componentHeight);
		
		dateBox.		 setBounds(labelWidth + 10,  40 * 2, 90, componentHeight);
		hourBox.		 setBounds(labelWidth + 125, 40 * 2, 40, componentHeight);
		minuteBox.		 setBounds(labelWidth + 180, 40 * 2, 40, componentHeight);
		runningMinuteBox.setBounds(labelWidth + 10,  40 * 3, 50, componentHeight);
		cinemaBox.		 setBounds(labelWidth + 10,  40 * 4, 70, componentHeight);
		
		descriptionPane.setBounds(0, 235, panelWidth, 165);
		
		panel.add(titleLabel);
		panel.add(priceLabel);
		panel.add(dateLabel);
		panel.add(runningTimeLabel);
		panel.add(cinemaLabel);
		panel.add(descriptionLabel);
		panel.add(euroLabel);
		panel.add(lineLabel);
		panel.add(dotsLabel);
		panel.add(minuteLabel);
		
		panel.add(titleField);
		panel.add(priceField);
		
		panel.add(dateBox);
		panel.add(hourBox);
		panel.add(minuteBox);
		panel.add(runningMinuteBox);
		panel.add(cinemaBox);
		
		panel.add(descriptionPane);
		
 		while(JOptionPane.showOptionDialog(null, panel, "Aggiungi spettacolo", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,	null, new String[] {"Ok", "Annulla"}, null) == 0)
 		{
 			GregorianCalendar current = new GregorianCalendar();
			GregorianCalendar dateStart = new GregorianCalendar(current.get(GregorianCalendar.YEAR),  current.get(GregorianCalendar.MONTH),
						(current.get(GregorianCalendar.WEEK_OF_MONTH) - 1) * 7 + dateBox.getSelectedIndex(), hourBox.getSelectedIndex(), minuteBox.getSelectedIndex());
				
			short runningTime = (short) (runningMinuteBox.getSelectedIndex() + 1);
				
			float price;
			
			try
			{
				price = Float.parseFloat(priceField.getText());
			}
			catch(NumberFormatException e)
			{
				JOptionPane.showMessageDialog(null, "Inserire un prezzo corretto.", "Errore", JOptionPane.ERROR_MESSAGE);
				continue;
			}
			
			try
			{		
				return new Show(titleField.getText(), price, dateStart, runningTime, cinemas.get(cinemaBox.getSelectedIndex()), descriptionArea.getText());
			}
			catch(IllegalArgumentException e)
			{
				JOptionPane.showMessageDialog(null, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
			}
 		}
 		
 		return null;
	}
	
	public static void resetFields()
	{
		titleField.setText("");
		priceField.setText("");
		descriptionArea.setText("");
		dateBox.setSelectedIndex(0);
		hourBox.setSelectedIndex(0);
		minuteBox.setSelectedIndex(0);
		runningMinuteBox.setSelectedIndex(0);
		cinemaBox.setSelectedIndex(0);
	}
}
