package s340.hardware.exception;

/*
 * Base class for exceptions thrown by the memory controller.
 */

public class MemoryFault extends Exception
{
	private static final long serialVersionUID = 1L;

	public MemoryFault(String message)
	{
		super(message);
	}

	public MemoryFault(int address)
	{
		this("address = " + address);
	}
}
