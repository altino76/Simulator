package s340.hardware;

/*
 * The interrupt registers for the S340 CPU.
 */
public class InterruptRegisters
{
	@SuppressWarnings("PublicField")
	public boolean register[];

	public InterruptRegisters(int n)
	{
		register = new boolean[ n ];
	}
}
