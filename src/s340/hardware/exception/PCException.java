package s340.hardware.exception;

/*
 * Exception throw when the pc is negative.
 */

public class PCException extends DecodingFault
{

	private static final long serialVersionUID = 1L;

	public PCException(int pc)
	{
		super(pc);
	}

}
