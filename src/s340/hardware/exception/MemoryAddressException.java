package s340.hardware.exception;

/*
 * Exception throw when the a memory address is negative or out of range.
 */

public class MemoryAddressException extends MemoryFault
{

	private static final long serialVersionUID = 1L;

	public MemoryAddressException(int address)
	{
		super(address);
	}

}
