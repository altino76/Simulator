package s340.hardware;

/*
 * The opcodes for the s340 CPU.
 */
public class Opcode
{
	public final static int LOAD = 0;
	public final static int LOADI = 1;
	public final static int LOADX = 2;
	public final static int STORE = 3;
	public final static int STOREX = 4;
	public final static int TAX = 5;
	public final static int TXA = 6;
	public final static int INCA = 7;
	public final static int INCX = 8;
	public final static int ADD = 9;
	public final static int ADDI = 10;
	public final static int SUB = 11;
	public final static int SUBI = 12;
	public final static int MUL = 13;
	public final static int DIV = 14;
	public final static int JMP = 15;
	public final static int JPOS = 16;
	public final static int JZERO = 17;
	public final static int JNEG = 18;
	public final static int SYSCALL = 19;
	public final static int END = 20;
	public final static int INPUT = 21;
	public final static int OUTPUT = 22;

	/*
	 * Text representation of opcodes.
	 */
	public final static String instructions[] =
	{
		"LOAD", "LOADI", "LOADX", "STORE", "STOREX", "TAX", "TXA", "INCA",
		"INCX", "ADD", "ADDI", "SUB", "SUBI", "MUL", "DIV", "JMP", "JPOS", "JZERO",
		"JNEG", "SYSCALL", "END",
		"INPUT", "OUTPUT"
	};

	/*
	 * Turn an (opcode, operand) instruction pair into a string representation.
	 */
	public static String toString(int pc, int opcode, int operand)
	{
		try
		{
			return String
					.format("%-3d:  %-8s%-4s", pc, Opcode.instructions[opcode], operand == -1 ? "" : " " + operand);
		}
		catch (Exception ex)
		{
			ex.printStackTrace(System.err);
			return "ERROR";
		}
	}
}
