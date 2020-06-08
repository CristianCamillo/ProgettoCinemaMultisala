package exceptions;

public class SeatStateNotChangedException extends RuntimeException
{
	private static final long serialVersionUID = -4349230117910604771L;
	
	public SeatStateNotChangedException()
	{
		super("Stato del posto non cambiato.");
	}
}
