package managerPackage;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import exportables.Cinema;
import exportables.Show;
import exportables.WeeklyProgram;

public final class CreateWeeklyProgramPane
{
	public static void create(ArrayList<Cinema> cinemas, WeeklyProgram program)
	{		
		if(cinemas == null)
			throw new NullPointerException("La lista delle sale cinematografiche può essere nulla.");
		if(program == null)
			throw new NullPointerException("Il programma settimanale non può essere nullo.");
		if(cinemas.size() == 0)
			throw new IllegalArgumentException("La lista delle sale cinematografiche non può essere vuota.");
		
		program.clear();
		
		JPanel panel = new JPanel(new BorderLayout());
		JTextArea area = new JTextArea(10, 20);
		JScrollPane pane = new JScrollPane(area);
		
		area.setEditable(false);
		
		panel.add(new JLabel("Spettacoli:"), BorderLayout.NORTH);
		panel.add(new JLabel(" "), BorderLayout.CENTER);
		panel.add(pane, BorderLayout.SOUTH);
		
		while(JOptionPane.showOptionDialog(null, panel, "Crea programma settimanale", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
					null, new String[] {"Aggiungi spettacolo", "Termina"}, null) == 0)
		{
			Show show = CreateShowPane.create(cinemas);
			
			if(show != null)
			{
				try
				{
					program.add(show);
					area.append(show.getTitle() + "\n");
					CreateShowPane.resetFields();
				}
				catch(IllegalArgumentException e)
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}
