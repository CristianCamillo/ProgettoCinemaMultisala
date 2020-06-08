package exceptions;

public class ListFullException extends RuntimeException
{
	private static final long serialVersionUID = -435785132704235247L;
	
	public ListFullException()
	{
		super("La lista è piena.");
	}
}
