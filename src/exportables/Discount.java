package exportables;

import java.io.Serializable;
import java.util.GregorianCalendar;
/**
 * Implementa una politica di sconto.<br>
 * <br>
 * Nello specifico vengono implementate la categoria alla quale si applica lo sconto, e la relatica percentuale.
 * 
 * @author Cristian
 *
 */
public final class Discount implements Cloneable, Serializable
{
	private static final long serialVersionUID = -4793470555722260816L;
	
	private final Category category;
	private final float percentage;
	/**
	 * Crea una nuova politica di sconto.
	 * 
	 * @param category Categoria alla quale si applica lo sconto.
	 * @param percentage Percentuale di sconto.
	 * @throws NullPointerException Se la categoria è {@code null}.
	 * @throws IllegalArgumentException Se la percentuale non è compresa tra 0 e 100.
	 */
	public Discount(Category category, float percentage)
	{
		if(category == null)
			throw new NullPointerException("La categoria non può essere nulla");
		
		if(percentage < 0 || percentage > 100)
			throw new IllegalArgumentException("La percentuale dev'essere compresa tra 0 e 100");
		
		this.category = category;
		this.percentage = percentage;
	}
	
	/**
	 * Ritorna la categoria alla quale si applica lo sconto.
	 * @return Categoria.
	 */
	
	public Category getCategory()
	{
		return category;
	}
	/**
	 * Ritorna la percentuale di sconto.
	 * @return Percentuale di sconto.
	 */
	
	public float getPercentage()
	{
		return percentage;
	}
	
	/**
	 * Ritorna il prezzo che il cliente deve pagare in base a questa politica di sconto
	 * @param show Spettacolo.
	 * @param customer Cliente.
	 * @return Prezzo che il cliente deve pagare secondo questa politica di sconto.
	 */
	
	public float generatePrice(Show show, Customer customer)
	{
		if(customer == null)
			throw new NullPointerException("Il cliente non può essere nullo.");
		
		float x = 0f;
		
		switch(category)
		{
			case MORNING:
				if(show.getDate().get(GregorianCalendar.HOUR_OF_DAY) >= 8 && show.getDate().get(GregorianCalendar.HOUR_OF_DAY) <= 12)
					x = percentage;
				break;
			case AFTERNOON:
				if(show.getDate().get(GregorianCalendar.HOUR_OF_DAY) >= 14 && show.getDate().get(GregorianCalendar.HOUR_OF_DAY) <= 18)
					x = percentage;
				break;
			case ADULT:
			case CHILD:
			case STUDENT:
			case RETIRED:
				if(customer.getCategory() == category)
					x = percentage;
				break;
			case MONDAY:
				if(show.getDate().get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.MONDAY)
					x = percentage;
				break;
			case TUESDAY:
				if(show.getDate().get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.TUESDAY)
					x = percentage;
				break;
			case WEDNESDAY:
				if(show.getDate().get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.WEDNESDAY)
					x = percentage;
				break;
			case THURSDAY:
				if(show.getDate().get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.THURSDAY)
					x = percentage;
				break;
			case FRIDAY:
				if(show.getDate().get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.FRIDAY)
					x = percentage;
				break;
			case SATURDAY:
				if(show.getDate().get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SATURDAY)
					x = percentage;
				break;
			case SUNDAY:
				if(show.getDate().get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SUNDAY)
					x = percentage;
		}

		return (((int)(show.getPrice() * (100f - x))) * 1f) / 100f;
	}
	
	/*OVERWRITTEN*/
	
	public String toString()
	{
		return getClass().getName() + "[category="+category+",percentage="+percentage+"]";
	}
	
	public boolean equals(Object obj)
	{
		if(obj == null || obj.getClass() != getClass()) return false;
		Discount test = (Discount)obj;
		return category.equals(test.getCategory());
	}
	
	public Discount clone()
	{
		try
		{
			return (Discount)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			return null;
		}
	}
}
