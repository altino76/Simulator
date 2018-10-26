package s340.hardware.exception;

/*
 * Exception thrown when the CPU tries to execute an unknown opcode.
 */

public class OpcodeException extends DecodingFault
{

	private static final long serialVersionUID = 1L;

	public OpcodeException(int pc)
	{
		super(pc);
	}

}
