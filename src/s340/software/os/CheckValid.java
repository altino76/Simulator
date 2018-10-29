package s340.software.os;

import s340.hardware.Machine;
import s340.hardware.Trap;

/*
 * Various methods to check arguments like trap numbers, device numbers, etc.
 */
public class CheckValid
{
	/*
	 * Check that a device number is valid.
	 *
	 * @param n -- the device number.
	 */

	public static  void deviceNumber(int n)
	{
		if (n < 0 || n >= Machine.NUM_DEVICES)
		{
			throw new IllegalArgumentException("Unknown device number : " + n);
		}
	}

	/*
	 * Check that a trap number is valid.
	 *
	 * @param n -- the trap number.
	 */
	public static void trapNumber(int n)
	{
		if (n < 0 || n >= Trap.NUM_TRAPS)
		{
			throw new IllegalArgumentException("Unknown trap number : " + n);
		}
	}

	/*
	 * Check that a system call number is valid.
	 *
	 * @param n -- the system call number.
	 */
	public static void syscallNumber(int n)
	{
		if (n < 0 || n >= SystemCall.NUM_SYSTEM_CALLS)
		{
			throw new IllegalArgumentException("Unknown system call number : " + n);
		}
	}
}
