package s340.hardware;

/*
 * A system call handler.
 */
public interface ISystemCallHandler
{
	void syscall(int savedProgramCounter, int callNumber);
}
