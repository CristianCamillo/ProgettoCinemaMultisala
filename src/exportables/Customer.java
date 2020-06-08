package exportables;
/**
 * 
 * Estende la class "Account".<br>
 * <br>
 * Aggiunge una categoria da associare ad un account.
 * 
 * @author Cristian
 *
 */
public final class Customer extends Account
{
	private static final long serialVersionUID = -8869324015629559554L;
	
	private Category category;
	/**
	 * 
	 * @param emailAddress Indirizzo email.
	 * @param password Password.
	 * @param category Categoria. 
	 * 
	 */
	public Customer(String emailAddress, String password, Category category)
	{		
		super(emailAddress, password);
		
		if(category == Category.ADULT || category == Category.CHILD || category == Category.STUDENT || category == Category.RETIRED || category == null)
			this.category = category;
		else
			throw new IllegalArgumentException("Categoria incorretta.");		
	}
	
	/**
	 * Ritorna la categoria.
	 * @return Categoria.
	 */	
	public Category getCategory()
	{
		return category;
	}
	
	/**
	 * 	Ritorna una rappresentazione sotto forma di {@link String} del cliente.
	 * @return Rappresentazione {@link String} del cliente.
	 */	
	public String toString()
	{
		return super.toString()+"[category="+category+"]";
	}
	
	/**
	 * Ritorna una copia di questo cliente.
	 * 
	 * @return Una copia di questo cliente.
	 */	
	public Customer clone()
	{
		return (Customer) super.clone();
	}
}
