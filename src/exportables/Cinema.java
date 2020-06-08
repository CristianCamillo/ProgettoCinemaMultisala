package exportables;

import java.io.Serializable;

import exceptions.SeatStateNotChangedException;

/**
 * Realizza una semplice sala cinematografica.<br>
 * <br>
 * Nello specifico vengono memorizzati il codice (numero progressivo) della sala,<br>
 * il numero di posti in orizzontale, il numero di posti in profondità,<br>
 * e lo stato per ogni singolo posto a sedere (disponibile, non disponibile, prenotato, acquistato).
 * @author Cristian
 *
 */

public final class Cinema implements Serializable, Cloneable, Comparable<Cinema>
{
	private static final long serialVersionUID = 7773159293980954846L;
	
	public static final byte MAX_WIDTH = 20;
	public static final byte MAX_DEPTH = 20;
	
	private byte code;
	private byte width;
	private byte depth;
	private String[][] seats;
	
	
	/**
	 * Crea un'istanza di questa classe.<br>
	 * <br>
	 * Inoltre, imposta tutti i posti a sedere disponibili. 
	 * 
	 * @param code Codice da assegnare alla sala.
	 * @param width Numero di posti in orizzontale.
	 * @param depth Numero di posti in verticale.
	 * @throws IllegalArgumentException Se il codice, la larghezza o la profondità sono non-positivi, oppure se la larghezza o la profondità eccedono i valori massimi preimpostati.
	 */
	public Cinema(byte code, byte width, byte depth) throws IllegalArgumentException
	{
		if(code <= 0 || width <= 0 || depth <= 0 || width > MAX_WIDTH || depth > MAX_DEPTH)
			throw new IllegalArgumentException("Argomenti invalidi.");
			
		this.code = code;
		this.width = width;
		this.depth = depth;
		seats = new String[depth][width];
		
		setAllSeatsAvailable();
	}
	
	/**
	 * @return Codice.
	 */
	public byte getCode()
	{
		return code;
	}
	
	/**
	 * @return Numero di posti in orizzontale.
	 */
	public byte getWidth()
	{
		return width;
	}
	
	/**
	 * @return Numero di posti in profondità.
	 */
	public byte getDepth()
	{
		return depth;
	}
	
	/**
	 * Controlla se un cliente ha un posto prenotato nella sala.
	 * @param customer Cliente.
	 * @return {@code true} se il cliente ha prenotato un posto a sedere, {@code false} altrimenti.
	 */
	public boolean isBookedBy(Customer customer)
	{
		for(byte y = 0; y < depth; y++)
			for(byte x = 0; x < width; x++)
				if(isSeatBookedBy(x, y, customer))
					return true;
		
		return false;
	}
	
	
	/**
	 * Controlla se un cliente ha un posto acquistato nella sala.
	 * @param customer Cliente.
	 * @return {@code true} se il cliente ha acquistato un posto a sedere, {@code false} altrimenti.
	 */
	public boolean isSoldTo(Customer customer)
	{
		for(byte y = 0; y < depth; y++)
			for(byte x = 0; x < width; x++)
				if(isSeatSoldTo(x, y, customer))
					return true;
		
		return false;
	}
	
	
	/**
	 * Controlla se un posto è disponibile.
	 * @param x Coordinata x.
	 * @param y Coordinata y.
	 * @return  {@code true} se il posto è disponibile, {@code false} altrimenti.
	 */
	public boolean isSeatAvailable(byte x, byte y)
	{
		return seats[y][x].equals("available");
	}
	

	/**
	 * Controlla se un posto non è disponibile.
	 * @param x Coordinata x.
	 * @param y Coordinata y.
	 * @return  {@code true} se il posto non è disponibile, {@code false} altrimenti.
	 */
	public boolean isSeatUnavailable(byte x, byte y)
	{
		return seats[y][x].equals("unavailable");
	}
	

	/**
	 * Controlla se un posto è prenotato.
	 * @param x Coordinata x.
	 * @param y Coordinata y.
	 * @return  {@code true} se il posto è prenotato, {@code false} altrimenti.
	 */
	public boolean isSeatBooked(byte x, byte y)
	{
		return seats[y][x].startsWith("booked");
	}
	
	/**
	 * Controlla se un posto è acquistato.
	 * @param x Coordinata x.
	 * @param y Coordinata y.
	 * @return  {@code true} se il posto è acquistato, {@code false} altrimenti.
	 */
	public boolean isSeatSold(byte x, byte y)
	{
		return seats[y][x].startsWith("sold");
	}
	
	/**
	 * Controlla se un posto è prenotato da un certo cliente.
	 * @param x Coordinata x.
	 * @param y Coordinata y.
	 * @param customer Cliente.
	 * @return  {@code true} se il posto è prenotato dal  cliente, {@code false} altrimenti.
	 */
	public boolean isSeatBookedBy(byte x, byte y, Customer customer)
	{
		return seats[y][x].startsWith("booked") && seats[y][x].substring(6).equals(customer.getEmailAddress());
	}
	
	/**
	 * Controlla se un posto è acquistato da un certo cliente.
	 * @param x Coordinata x.
	 * @param y Coordinata y.
	 * @param customer Cliente.
	 * @return  {@code true} se il posto è acquistato dal  cliente, {@code false} altrimenti.
	 */
	public boolean isSeatSoldTo(byte x, byte y, Customer customer)
	{
		return seats[y][x].startsWith("sold") && seats[y][x].substring(4).equals(customer.getEmailAddress());
	}
	
	/**
	 * Rende un posto disponibile.
	 * @param x Coordinata x.
	 * @param y Coordinata y.
	 * @throws SeatStateNotChangedException Se in posto non era non-disponibile.
	 */	
	public void setSeatAvailable(byte x, byte y)
	{
		if(isSeatUnavailable(x, y))
			seats[y][x] = "available";
		else
			throw new SeatStateNotChangedException();
	}
	
	/**
	 * Rende un posto non-disponibile.
	 * @param x Coordinata x.
	 * @param y Coordinata y.
	 * @throws SeatStateNotChangedException Se in posto non era disponibile.
	 */	
	public void setSeatUnavailable(byte x, byte y)
	{
		if(isSeatAvailable(x, y))
			seats[y][x] = "unavailable";
		else
			throw new SeatStateNotChangedException();
	}
	
	
	/**
	 * Rende un posto prenotato da un certo cliente.
	 * @param x Coordinata x.
	 * @param y Coordinata y.
	 * @throws SeatStateNotChangedException Se in posto non era disponibile.
	 */	
	public void setSeatBooked(byte x, byte y, String id)
	{
		if(isSeatAvailable(x, y))		
			seats[y][x] = "booked" + id;
		else
			throw new SeatStateNotChangedException();
	}
	
	/**
	 * Rende un posto acquistato da un certo cliente.
	 * @param x Coordinata x.
	 * @param y Coordinata y.
	 * @throws SeatStateNotChangedException Se in posto non era stato prenotato dal cliente.
	 */	
	public void setSeatSold(byte x, byte y, Customer customer)
	{
		if(isSeatBookedBy(x, y, customer))
			seats[y][x] = "sold" + customer.getEmailAddress();
		else
			throw new SeatStateNotChangedException();
	}
	
	
	/**
	 * Rimuova la prenotazione di un posto da parte di un certo cliente.
	 * @param x Coordinata x.
	 * @param y Coordinata y.
	 * @throws SeatStateNotChangedException Se in posto non era prenotato dal cliente.
	 */	
	public void removeBook(byte x, byte y, Customer customer)
	{
		if(isSeatBookedBy(x, y, customer))
			seats[y][x] = "available";
		else
			throw new SeatStateNotChangedException();
	}
	
	/**
	 * Rimuova l'acquisto di un posto da parte di un certo cliente.
	 * @param x Coordinata x.
	 * @param y Coordinata y.
	 * @throws SeatStateNotChangedException Se in posto non era acquistato dal cliente.
	 */	
	public void removeSold(byte x, byte y, Customer customer)
	{
		if(isSeatSoldTo(x, y, customer))
			seats[y][x] = "available";
		else
			throw new SeatStateNotChangedException();
	}
	
	/**
	 * Rimuove la prenotazione di un posto da parte di un certo cliente.
	 * @throws SeatStateNotChangedException Se nessun posto era prenotato dal cliente.
	 */	
	public void removeBook(Customer customer)
	{
		for(byte y = 0; y < depth; y++)
			for(byte x = 0; x < width; x++)
				if(isSeatBookedBy(x, y, customer))
				{
					removeBook(x, y, customer);
					return;
				}
		
		throw new SeatStateNotChangedException();
	}
	
	/**
	 * Rende tutti i posti disponibili.
	 */	
	public void setAllSeatsAvailable()
	{
		for(byte y = 0; y < depth; y++)
			for(byte x = 0; x < width; x++)
				seats[y][x] = "available";
	}
	
	
	/**
	 * Rimuove tutte le prenotazioni.
	 */	
	public void removeAllBooks()
	{
		for(byte y = 0; y < depth; y++)
			for(byte x = 0; x < width; x++)
				if(isSeatBooked(x, y))
					seats[y][x] = "available";
	}
	
	/**
	 * @return Numero di posti disponibili.
	 */
	
	public short getFreeSeatsCount()
	{
		short counter = 0;
		
		for(byte y = 0; y < depth; y++)
			for(byte x = 0; x < width; x++)
				if(seats[y][x].equals("available"))
					counter++;
		
		return counter;
	}
	
	/**
	 * 	Ritorna una rappresentazione sotto forma di {@link String} della sala cinematografica.
	 * @return Rappresentazione {@link String} della sala.
	 */
	
	public String toString()
	{
		return getClass().getName() + "[code="+code+",width="+width+",depth="+depth+",seats="+seats+"]";
	}
	
	/**
	 * Verifica se un oggetto è equivalente alla presente sala.<br>
	 * Nello specifico, vengono soltanto confrontati i codici.
	 * 
	 * @param obj oggetto da confrontare.
	 * @return {@code true} se l'oggetto corrisponde, {@code false} altrimenti.
	 */
	public boolean equals(Object obj)
	{
		if(obj == null || obj.getClass() != getClass()) return false;
		Cinema test = (Cinema)obj;
		return test.getCode() == code;
	}
	
	/**
	 * Ritorna una copia di questa sala cinematografica.
	 * 
	 * @return Una copia di questa sala.
	 */
	public Cinema clone()
	{
		try
		{
			Cinema clone = (Cinema) super.clone();
			
			String[][] clonedSeats = new String[depth][width];
			for(byte y = 0; y < depth; y++)
				for(byte x = 0; x < width; x++)
					clonedSeats[y][x] = seats[y][x];
			clone.seats = clonedSeats;
			
			return clone;
		}
		catch(CloneNotSupportedException e)
		{
			return null;
		}
	}

	/**
	 * Confronta la sala cinematografica con quella ricevuta per determinarne l'ordine.<br>
	 * Nello specifico, vengono confrontati soltanto i codici.
	 * 
	 * @param cinema La sala da confrontare.
	 * @return 0 se i codici corrispondo,<br>
	 * -1 se il codice di questa sala è minore del codice dell'altra sala che si sta confrontando,<br>
	 * 1 se il codice di questa sala è maggiore del codice dell'altra sala che si sta confrontando.<br>  
	 */
	public int compareTo(Cinema cinema)
	{
		if(cinema == null)
			throw new NullPointerException("Impossibile comparare con un oggetto nullo.");
		
		return code == cinema.getCode() ? 0 : code < cinema.getCode() ? -1 : 1;		
	}
}
