package exportables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import exceptions.ListFullException;
/**
 * Implementa un programma settimanale.
 * 
 * Memorizza una lista di spettacoli ed una lista dei corrispettivi guadagni.
 * @author Cristian
 *
 */
public final class WeeklyProgram implements Serializable
{
	private static final long serialVersionUID = -8500587838624458770L;

	private GregorianCalendar startingDate;
	
	private ArrayList<Show> shows = new ArrayList<Show>();
	private ArrayList<Float> earnings = new ArrayList<Float>();
	
	private boolean discount = true;
	
	/**
	 * Genera la data del presente lunedì, in modo da salvare la data di inizio del presente programma/settimana.	 * 
	 */
	public WeeklyProgram()
	{
		GregorianCalendar now = new GregorianCalendar();		
		startingDate = new GregorianCalendar(now.get(GregorianCalendar.YEAR), now.get(GregorianCalendar.MONTH), (now.get(GregorianCalendar.WEEK_OF_MONTH) - 1) * 7, 0, 0);
	}
	
	/**
	 * 
	 * @return Data di inizio.
	 */
	public GregorianCalendar getStartingDate()
	{
		return (GregorianCalendar) startingDate.clone();
	}
	
	/**
	 * Aggiunge uno spettacolo alla lista.
	 * @param show Spettacolo da aggiungere alla lista.
	 * @throws ListFullException Se si supera il limite massimo di spettacoli.
	 * @throws NullPointerException Se lo spettacolo ricevuto è nullo.
	 * @throws IllegalArgumentException Se è già presente uno spettacolo con lo stesso titolo.
	 */
	public void add(Show show)
	{
		if(shows.size() >= Short.MAX_VALUE)
			throw new ListFullException();
			
		if(show == null)
			throw new NullPointerException("Lo spettacolo da aggiungere al programma non può essere nullo.");
		
		GregorianCalendar endingDate = ((GregorianCalendar)startingDate.clone());
		endingDate.add(GregorianCalendar.DAY_OF_WEEK, 7);
		
		if(show.getDate().compareTo(startingDate) < 0 || show.getDate().compareTo(endingDate) > 0)
			throw new IllegalArgumentException("Questo spettacolo non può essere aggiunto al programma settimanale perché la sua data è incompatibile.");
		
		for(short i = 0; i < shows.size(); i++)
		{
			if(show.getTitle().equals(shows.get(i).getTitle()))
				throw new IllegalArgumentException("E' già presente uno spettacolo con lo stesso titolo.");
			
			if(shows.get(i).getCinema().compareTo(show.getCinema()) == 0)
			{
				GregorianCalendar showStartDate = shows.get(i).getDate();
				GregorianCalendar showEndDate = ((GregorianCalendar) shows.get(i).getDate().clone());
				showEndDate.add(GregorianCalendar.MINUTE, shows.get(i).getRunningTime());
				GregorianCalendar newShowEndDate = ((GregorianCalendar) show.getDate().clone());
				newShowEndDate.add(GregorianCalendar.MINUTE, show.getRunningTime());
				
				if(showStartDate.compareTo(show.getDate()) <= 0 && show.getDate().compareTo(showEndDate) <= 0 ||
				   showStartDate.compareTo(newShowEndDate) <= 0 && newShowEndDate.compareTo(showEndDate) <= 0 ||
				   showStartDate.compareTo(show.getDate()) >= 0 && newShowEndDate.compareTo(showEndDate) >= 0)
					throw new IllegalArgumentException("La sala è già occupata.");
			}
		}
		
		shows.add(show.clone());
		earnings.add(0f);
	}
	
	/**
	 * Ritorna lo spettacolo di indirizzo "index".
	 * @param index Indice dello spettacolo nella lista.
	 * @return Spettacolo di indirizzo "index".
	 * @throws IndexOutOfBoundsException Se l'indirizzo è invalido.
	 */
	public Show get(int index)
	{
		return shows.get(index);
	}
	
	/**
	 * Ritorna lo spettacolo inserito prelevandolo dalla lista.
	 * @param show Spettacolo per ricerca.
	 * @return Spettacolo dalla lista.
	 */
	public int indexOf(Show show)
	{
		return shows.indexOf(show); 
	}
	
	/**
	 * Ripulisce la lista di spettacoli e guadagni.
	 */
	public void clear()
	{
		shows.clear();
		earnings.clear();
	}
	
	/**
	 * @return Numero di posti liberi.
	 */
	public short getNumberOfShows()
	{
		return (short) shows.size();
	}
	
	/**
	 * Attiva le politiche di sconto.
	 */
	public void enableDiscount()
	{
		discount = true;
	}
	
	/**
	 * Disattiva le politiche di sconto.
	 */
	
	public void disableDiscount()
	{
		discount = false;
	}
	
	/**
	 * @return {@code true} se le politiche di sconto sono abilitate, {@code false} altrimenti.
	 */
	
	public boolean discountEnabled()
	{
		return discount;
	}
	
	/**
	 * Aggiunge uno guadagno al dato spettacolo.
	 * @param show Spettacolo al quale aggiungere il guadagno.
	 * @param earning Guadagno da aggiungere.
	 * @throws NullPointerException Se lo spettacolo è nullo.
	 * @throws IllegalArgumentException Se lo spettacolo ricevuto non è presente nel programma settimanale, oppure se il guadagno è negativo o ha cifre sotto i centesimi.
	 * 
	 */
	public void registerEarning(Show show, float earning)
	{
		if(show == null)
			throw new NullPointerException("Lo spettacolo non può essere nullo.");
		
		int index = shows.indexOf(show);
		
		if(index == -1)
			throw new IllegalArgumentException("Questo spettacolo non è presente in questo programma settimanale.");
		
		if(earning < 0f)
			throw new IllegalArgumentException("Il guadagno non può essere negativo.");
		
		if(earning * 100 != (int)(earning * 100))
			throw new IllegalArgumentException("Il guadagno non può scendere sotto i centesimi.");
		
		earnings.set(index, earnings.get(index) + earning);
	}
	
	/**
	 * 
	 * @return Guadagno totale.
	 */
	public float getTotalEarning()
	{
		float total = 0f;
		
		for(short i = 0; i < earnings.size(); i++)
			total += earnings.get(i);
		
		return total;
	}
	
	/**
	 * 
	 * @return Lista dei titoli degli spettacoli con i relativi guadagni.
	 */
	public ArrayList<?>[] getEarningsByShow()
	{
		if(shows.size() > 0)
		{
			ArrayList<?>[] showsAndEarnings = new ArrayList[2];
			ArrayList<String> clonedShowTitles = new ArrayList<String>();
			ArrayList<Float> clonedEarnings = new ArrayList<Float>();
			
			for(short i = 0; i < shows.size(); i++)
			{
				clonedShowTitles.add(shows.get(i).getTitle());
				clonedEarnings.add(earnings.get(i) + 0f);
			}
			
			showsAndEarnings[0] = clonedShowTitles;
			showsAndEarnings[1] = clonedEarnings;
			
			return showsAndEarnings;
		}
		else
			return null;
	}
}
