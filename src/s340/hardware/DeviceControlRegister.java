package s340.hardware;

import java.util.concurrent.Semaphore;

/*
 * An I/O device control register.
 */
@SuppressWarnings("PublicField")
public class DeviceControlRegister
{
	public int[] register;
	//	do not touch this
	protected final Semaphore startOperation;

	public DeviceControlRegister(int n)
	{
		register = new int[ n ];
		startOperation = new Semaphore(0);
	}

	//	latch the device to start its operation
	public void latch()
	{
		startOperation.release();
	}
}
