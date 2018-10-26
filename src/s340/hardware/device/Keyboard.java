package s340.hardware.device;

import s340.hardware.Device;
import s340.hardware.DeviceControlRegister;
import s340.hardware.InterruptRegisters;

import java.util.Scanner;

/*
 * A keyboard device.
 */
public class Keyboard extends Device
{
	private final Scanner input;

	public Keyboard(int deviceNumber, InterruptRegisters interruptRegisters, DeviceControlRegister controlRegister)
	{
		super(deviceNumber, interruptRegisters, controlRegister);
		input = new Scanner(System.in);
	}

	/*
	 * Read an integer into device register[1].
	 */
	@Override
	protected void doOperation()
	{
		System.out.println("Input value : ");
		int value = input.nextInt();
		controlRegister.register[1] = value;
	}
}
