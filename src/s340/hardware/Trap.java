package s340.hardware;

/*
 * Traps that can be issued by the S340 CPU.
 */
public interface Trap
{
	public final static int TIMER = 0;
	public final static int DECODE_FAULT = 1;
	public final static int CPU_FAULT = 2;
	public final static int MEMORY_FAULT = 3;
	public final static int END = 4;
	public final static int DIV_ZERO = 5;

	public final static int NUM_TRAPS = 6;
}
