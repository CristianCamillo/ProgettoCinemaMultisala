package exceptions;

public class RegistrationCanceledException extends RuntimeException
{
	private static final long serialVersionUID = -2457174586583069841L;

	public RegistrationCanceledException()
	{
		super("La registrazione è stata annullata.");
	}
}
