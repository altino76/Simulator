package s340.hardware.exception;

/*
 * Base class for exceptions thrown by the CPU during instruction decoding.
 */

public class DecodingFault extends Exception
{

	private static final long serialVersionUID = 1L;

	public DecodingFault(String message)
	{
		super(message);
	}

	public DecodingFault(int pc)
	{
		this("pc = " + pc);
	}

}
