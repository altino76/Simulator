package s340.hardware;

import s340.hardware.exception.MemoryAddressException;
import s340.hardware.exception.MemoryFault;

/*
 * A basic memory controller.
 */
public class MemoryController
{
	private final int[] memory;
	private int base = 0;
	private int limit;

	public MemoryController(int[] contents)
	{
		this.memory = contents;
		//	initial configuration so that virtual and physical addresses are the same thing
		this.base = 0;
		this.limit = memory.length;
	}

	public MemoryController(int size)
	{
		this(new int[ size ]);
	}

	public void setBase(int base)
	{
		this.base = base;
	}

	public void setLimit(int limit)
	{
		this.limit = limit;
	}

	/*
	 * Check if a physical memory address is valid.
	 */
	private void checkAddress(int virtualAddress) throws MemoryAddressException
	{
		if (virtualAddress < 0 || virtualAddress >= limit)
		{
			throw new MemoryAddressException(virtualAddress);
		}
	}

	/*
	 * Load the contents of a given virtual memory address.
	 */
	public int load(int address) throws MemoryFault
	{
		checkAddress(address);
		return memory[base + address];
	}

	/*
	 * Store a value into a given virtual memory address.
	 */
	public void store(int address, int value) throws MemoryFault
	{
		checkAddress(address);
		memory[base + address] = value;
	}

    public int getBase() {
        return base;
    }

    public int getLimit() {
        return limit;
    }
}