package s340.hardware.device;

import s340.hardware.Device;
import s340.hardware.DeviceControlRegister;
import s340.hardware.InterruptRegisters;

/*
 * A console device.
 */
public class Console extends Device
{
	public Console(int deviceNumber, InterruptRegisters interruptRegisters, DeviceControlRegister controlRegisters)
	{
		super(deviceNumber, interruptRegisters, controlRegisters);
	}

	/*
	 * Write the contents of device register[1] to the screen.
	 */
	@Override
	protected void doOperation()
	{
		System.out.println("Output value : " + controlRegister.register[1]);
	}
}
