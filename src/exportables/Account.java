package exportables;

import java.io.Serializable;
/**
 * Semplice implementazione di un account.<br>
 * <br>
 * Vengono memorizzati un indirizzo email ed una password.
 * 
 * 
 * @author Cristian
 *
 */
public class Account implements Serializable, Cloneable, Comparable<Account>
{
	private static final long serialVersionUID = 2686664455985702866L;
	
	protected String emailAddress;
	protected String password;
	
	/**
	 * Crea un account dato un'indirizzo email ed una password.
	 * 
	 * @param emailAddress Indirizzo email.
	 * @param password Password.
	 * @throws NullPointerException Se almeno uno dei due tra indirizzo e password sia {@code null}.
	 * @throws IllegalArgumentException Se l'indirizzo email non è in un formato corretto (4+chars@4+chars.2+chars) o la password non composta da almeno 6 caratteri.
	 */
	
	public Account(String emailAddress, String password)
	{
		if(emailAddress == null)
			throw new NullPointerException("L'indirizzo email non può essere nullo.");
		if(password == null)
			throw new NullPointerException("La password non può essere nulla.");
		
		emailAddress = emailAddress.toLowerCase();
		
		if(!validEmailAddress(emailAddress))
			throw new IllegalArgumentException("L'indirizzo email non è valido.");
		
		if(password.length() < 6)
			throw new IllegalArgumentException("La password deve avere almeno 6 caratteri.");
		
		this.emailAddress = emailAddress;
		this.password = password;
	}
	
	/**
	 * @return Indirizzo email.
	 */
	
	public String getEmailAddress()
	{
		return emailAddress;
	}
	
	/**
	 * Verifica se la stringa ricevuta corrisponde alla password memorizzata.
	 * @param str Stringa da raffrontare.
	 * @return {@code true} se le password corrispondono, {@code false} altrimenti.
	 */
	
	public boolean checkPassword(String str)
	{
		return this.password.equals(str);
	}
	
	/**
	 * Verfica se l'indirizzo email ricevuto è in un formato corretto (4+chars@4+chars.2+chars).
	 * @param address Indirizzo email da verficare.
	 * @return {@code true} se l'indirizzo email è in un formato corretto, {@code false} altrimenti.
	 */
	
	private static boolean validEmailAddress(String address)
	{	
		if(address.isBlank())
			return false;
		
		if(address.indexOf('@') == -1                                     ||
		   address.indexOf('@') > address.indexOf('.')                    ||
		   address.substring(address.indexOf('@') + 1).indexOf('@') != -1 ||
		   address.substring(address.indexOf('.') + 1).indexOf('.') != -1)
			return false;
			
		String username = address.substring(0, address.indexOf('@'));
		String domain = address.substring(address.indexOf('@') + 1, address.indexOf('.'));
		String ext = address.substring(address.indexOf('.') + 1, address.length());

		if(username.length() >= 4 && domain.length() >= 4 && ext.length() >= 2)
			return true;
		else
			return false;
	}
	
	
	/**
	 * 	Ritorna una rappresentazione sotto forma di {@link String} dell'account.
	 * @return Rappresentazione {@link String} dell'account.
	 */
	public String toString()
	{
		return getClass().getName()+"[emailAddress="+emailAddress+",password="+password+"]";
	}
	
	/**
	 * Verifica se un oggetto è equivalente al presente Account.<br>
	 * Nello specifico, vengono confrontati sia l'indirizzo email che la password.
	 * 
	 * @param obj oggetto da confrontare.
	 * @return {@code true} se l'oggetto corrisponde, {@code false} altrimenti.
	 */
	
	public boolean equals(Object obj)
	{
		if(obj == null || getClass() != obj.getClass()) return false;
		Account test = (Account)obj;
		return emailAddress.equals(test.getEmailAddress()) && test.checkPassword(password);
	}
	
	/**
	 * Ritorna una copia di quest'account.
	 * 
	 * @return Una copia di quest'account.
	 */
	
	public Account clone()
	{
		try
		{
			return (Account)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			return null;
		}
	}

	
	/**
	 * Confronta l'account con quello ricevuto per determinarne l'ordine.<br>
	 * Nello specifico, vengono confrontati soltanto gli indirizzi email.
	 * 
	 * @param account L'account da confrontare.
	 * @return 0 se gli indirizzi email corrispondo,<br>
	 * -1 se l'indirizzo di questo account precede quello dell'account che si sta confrontando,<br>
	 * 1 se l'indirzzo di questo account segue quello dell'account che si sta confrontando.<br>  
	 */
	public int compareTo(Account account)
	{
		if(account == null)
			throw new NullPointerException("Impossibile comparare con un account nullo.");
		
		return emailAddress.compareTo(account.getEmailAddress());
	}
}
