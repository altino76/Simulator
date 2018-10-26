package s340.hardware.device;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import s340.hardware.Device;
import s340.hardware.DeviceControlRegister;
import s340.hardware.DeviceControllerOperations;
import s340.hardware.InterruptRegisters;

/*
 * A disk drive device with 10 platters, each holding 100 ints.
 */
public class Disk extends Device
{
    public final static int NUM_PLATTERS = 10;
    public final static int PLATTER_SIZE = 100;

    private final static String PLATTER = "P";

    private String F(int platterNumber)
    {
        return "F-" + deviceNumber + "-" + PLATTER + platterNumber;
    }

    public Disk(int deviceNumber, InterruptRegisters interruptRegisters, DeviceControlRegister controlRegisters,
        int[] buffer) throws IOException
    {
        super(deviceNumber, interruptRegisters, controlRegisters, buffer);
        // if we don't have the platter files, create them
        if (!new File(F(0)).exists())
        {
            System.err.println("WRITING INITIAL FILES");
            for (int i = 0; i < NUM_PLATTERS; i++)
            {
                writePlatter(i, new int[PLATTER_SIZE]);
            }
        }
    }

    private int[] readPlatter(int platterNum) throws IOException
    {
        int[] result = new int[PLATTER_SIZE];
        try (DataInputStream dos = new DataInputStream(new FileInputStream(F(platterNum))))
        {
            for (int i = 0; i < PLATTER_SIZE; i++)
            {
                result[i] = dos.readInt();
            }
        }

        return result;
    }

    private void writePlatter(int platterNum, int[] data) throws IOException
    {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(F(platterNum))))
        {
            for (int i = 0; i < PLATTER_SIZE; i++)
            {
                dos.writeInt(data[i]);
            }
        }
    }

    @Override
    protected void doOperation() throws IOException
    {
        int operation = controlRegister.register[0];
        int platter = controlRegister.register[1];
        int start = controlRegister.register[2];
        int length = controlRegister.register[3];

        switch (operation)
        {
            case DeviceControllerOperations.READ:
                System.arraycopy(readPlatter(platter), start, buffer, 0, length);
                break;
            case DeviceControllerOperations.WRITE:
                int[] data = readPlatter(platter);
                System.arraycopy(buffer, 0, data, start, length);
                writePlatter(platter, data);
                break;
            default:
                throw new IllegalArgumentException("Bad operation " + operation);
        }
    }
}
