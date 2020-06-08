package exportables;

import java.io.Serializable;
import java.util.GregorianCalendar;
/**
 * Implementa uno spettacolo.<br>
 * <br>
 * Nello specifico memorizza il titolo, il prezzo di base, la data di svolgimento, la durata in minuti, la sala in cui si terrà e una descrizione.
 * 
 * @author Cristian
 *
 */
public final class Show implements Serializable, Cloneable, Comparable<Show>
{
	private static final long serialVersionUID = 1L;
	
	private final String title;
	private final float price;
	private GregorianCalendar date;
	private final short runningTime;
	private Cinema cinema;
	private final String description;	
	
	/**
	 * Istanzia un nuovo spettacolo con i relatici parametri
	 * @param title Titolo.
	 * @param price Prezzo di base.
	 * @param date Data
	 * @param runningTime Durata in minuti.
	 * @param cinema Sala in cui si tiene lo spettacolo.
	 * @param description Descrizione.
	 */
	public Show(String title, float price, GregorianCalendar date, short runningTime, Cinema cinema, String description)
	{
		if(title == null)
			throw new NullPointerException("Il titolo dello spettacolo non può essere nullo.");
		
		if(title.isBlank())
			throw new IllegalArgumentException("Il titolo dello spettacolo non può essere vuoto.");
		
		if(price < 0)
			throw new IllegalArgumentException("Il prezzo non può essere negativo.");
		
		if(price * 100 != (int)(price * 100))
			throw new IllegalArgumentException("Il prezzo non può scendere sotto i centesimi.");
		
		if(date == null)
			throw new NullPointerException("La data dello spettacolo non può essere nulla.");
		
		if(runningTime <= 0)
			throw new IllegalArgumentException("La durata dello spettacolo dev'essere positiva.");
		
		if(cinema == null)
			throw new IllegalArgumentException("La sala cinematografica non può essere nulla.");
		
		if(description == null)
			throw new NullPointerException("La descrizione non può essere nulla");
		
		this.title = title;
		this.price = price;		
		this.date = date;
		this.runningTime = runningTime;
		this.cinema = cinema;
		this.description = description;
	}
	
	/**
	 * @return Titolo.
	 */
	
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * @return Prezzo di base.
	 */
	public float getPrice()
	{
		return price;
	}
	
	/**
	 * 
	 * @return Data.
	 */
	public GregorianCalendar getDate()
	{
		return (GregorianCalendar)date.clone();
	}
	
	/**
	 * 
	 * @return Durata in minuti.
	 */
	
	public short getRunningTime()
	{
		return runningTime;
	}
	
	/**
	 * 
	 * @return Sala cinematografica.
	 */
	
	public Cinema getCinema()
	{
		return cinema;
	}
	
	/**
	 * 
	 * @return Descrizione.
	 */
	
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Imposta una nuova sala cinematografica.
	 * @param cinema Nuona sala.
	 */
	
	public void setCinema(Cinema cinema)
	{
		this.cinema = cinema;
	}
	
	/**
	 * 	Ritorna una rappresentazione sotto forma di {@link String} dello spettacolo.
	 * @return Rappresentazione {@link String} dello spettacolo.
	 */	
	
	public String toString()
	{
		return getClass().getName() + "[title="+title+",price="+price+",date="+date+",runningTime="+runningTime+",cinema"+cinema+",description="+description+"]";
	}
	
	/**
	 * Verifica se un oggetto è equivalente al presente spettacolo.<br>
	 * Nello specifico, vengono confrontati soltanto i titoli.
	 * 
	 * @param obj oggetto da confrontare.
	 * @return {@code true} se l'oggetto corrisponde, {@code false} altrimenti.
	 */
	public boolean equals(Object obj)
	{
		if(obj == null || getClass() != obj.getClass()) return false;
		Show test = (Show)obj;
		return title.equals(test.getTitle());
	}
	
	/**
	 * Ritorna una copia di questo spettacolo.
	 * 
	 * @return Una copia di questo spettacolo.
	 */	
	public Show clone()
	{
		try
		{
			Show clone = (Show) super.clone();
			clone.date = (GregorianCalendar) this.date.clone();
			clone.cinema = (Cinema) this.cinema.clone();
			return clone;
		}
		catch(CloneNotSupportedException e)
		{
			return null;
		}
	}

	/**
	 * Confronta lo spettacolo con quello ricevuto per determinarne l'ordine.<br>
	 * Nello specifico, vengono confrontati soltanto i titoli.
	 * 
	 * @param account Lo spettacolo da confrontare.
	 * @return 0 se i titoli corrispondono,<br>
	 * -1 se il titolo di questo spettacolo precede quello dello spettacolo che si sta confrontando,<br>
	 * 1 se il titolo di questo spettacolo segue quello dello spettacolo che si sta confrontando.<br>  
	 */
	public int compareTo(Show show)
	{
		if(show == null)
			throw new NullPointerException("Impossibile comparare con uno spettacolo nullo.");
		
		return title.compareTo(show.getTitle());		
	}
}
