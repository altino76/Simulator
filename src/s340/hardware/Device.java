package s340.hardware;

/*
 * Base class for I/O devices.
 */
@SuppressWarnings("PublicField")
public abstract class Device implements Runnable
{
	//	the device number of this device
	public int deviceNumber;

	//	a memory buffer for devices that need it
	public int[] buffer;

	//	the CPU interrupt registers
	public InterruptRegisters interruptRegisters;

	//	the device control register for this device
	public DeviceControlRegister controlRegister;

	public Device(int deviceNumber, InterruptRegisters interruptRegisters,
			DeviceControlRegister controlRegister, int[] buffer)
	{
		this.deviceNumber = deviceNumber;
		this.interruptRegisters = interruptRegisters;
		this.controlRegister = controlRegister;
		this.buffer = buffer;
	}

	public Device(int deviceNumber, InterruptRegisters interruptRegisters, DeviceControlRegister controlRegister)
	{
		this(deviceNumber, interruptRegisters, controlRegister, null);
	}

	//	do not use this method
	protected abstract void doOperation() throws Exception;

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				//	wait for a start operation signal
				controlRegister.startOperation.acquire();
				//	process the operation
				doOperation();
				//	inform the CPU that we have finished the operation
				interruptRegisters.register[deviceNumber] = true;
			}
			catch (Exception ex)
			{
				ex.printStackTrace(System.err);
				System.exit(0);
			}
		}
	}
}
