package s340.software.os;

import s340.hardware.Opcode;

/*
 * An S340 assembly language program.
 */
public class Program
{
	//	the code
	private final int[] code;
	//	the start address
	private int start;
	//	the data size
	private final int dataSize;

	public Program(int[] code, int start, int dataSize)
	{
		this.code = code;
		this.start = start;
		this.dataSize = dataSize;
	}

	@SuppressWarnings("ReturnOfCollectionOrArrayField")
	public int[] getCode()
	{
		return code;
	}
	public int getStart()
	{
		return start;
	}

	public int getDataSize()
	{
		return dataSize;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < getCode().length; i += 2)
		{
			int opcode = getCode()[i];
			int operand = getCode()[i + 1];
			builder.append(Opcode.toString(start + i, opcode, operand));
			builder.append("\n");
		}

		return builder.toString();
	}
}
