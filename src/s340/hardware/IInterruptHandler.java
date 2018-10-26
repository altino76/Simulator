package s340.hardware;

/*
 * An interrupt handler.
 */
public interface IInterruptHandler
{
	void interrupt(int savedProgramCounter, int deviceNumber);
}
