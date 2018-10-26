package s340.hardware;

/*
 * A trap handler.
 */
public interface ITrapHandler
{
	void trap(int savedProgramCounter, int trapNumber);
}
