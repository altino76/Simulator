package s340.software.os;

import java.util.LinkedList;

import s340.hardware.Opcode;

/**
 * Class to help programmers write assembly language programs for the S340 CPU.
 */
public class ProgramBuilder
{
	private final LinkedList<Integer> instructions;
	private int start;
	private int size;

	public ProgramBuilder()
	{
		instructions = new LinkedList<>();
		size = 0;
		start = 0;
	}

	private void instruction(int opcode, int operand)
	{
		instructions.add(opcode);
		instructions.add(operand);
	}

	private void instruction(int opcode)
	{
		instruction(opcode, -1);
	}

	private int S()
	{
		return instructions.size() - 2 + start;
	}

	public int load(int address)
	{
		instruction(Opcode.LOAD, address);
		return S();
	}

	public int loadi(int value)
	{
		instruction(Opcode.LOADI, value);
		return S();
	}

	public int loadx(int value)
	{
		instruction(Opcode.LOADX, value);
		return S();
	}

	public int store(int address)
	{
		instruction(Opcode.STORE, address);
		return S();
	}

	public int storex(int address)
	{
		instruction(Opcode.STOREX, address);
		return S();
	}

	public int tax()
	{
		instruction(Opcode.TAX);
		return S();
	}

	public int txa()
	{
		instruction(Opcode.TXA);
		return S();
	}

	public int inca()
	{
		instruction(Opcode.INCA);
		return S();
	}

	public int incx()
	{
		instruction(Opcode.INCX);
		return S();
	}

	public int add(int address)
	{
		instruction(Opcode.ADD, address);
		return S();
	}

	public int addi(int value)
	{
		instruction(Opcode.ADDI, value);
		return S();
	}

	public int sub(int address)
	{
		instruction(Opcode.SUB, address);
		return S();
	}

	public int subi(int value)
	{
		instruction(Opcode.SUBI, value);
		return S();
	}

	public int mul(int address)
	{
		instruction(Opcode.MUL, address);
		return S();
	}

	public int div(int address)
	{
		instruction(Opcode.DIV, address);
		return S();
	}

	public int input()
	{
		instruction(Opcode.INPUT);
		return S();
	}

	public int output()
	{
		instruction(Opcode.OUTPUT);
		return S();
	}

	public int jmp(int address)
	{
		instruction(Opcode.JMP, address);
		return S();
	}

	public int jpos(int address)
	{
		instruction(Opcode.JPOS, address);
		return S();
	}

	public int jzero(int address)
	{
		instruction(Opcode.JZERO, address);
		return S();
	}

	public int jneg(int address)
	{
		instruction(Opcode.JNEG, address);
		return S();
	}

	public int end()
	{
		instruction(Opcode.END);
		return S();
	}

	public int syscall(int callNumner)
	{
		instruction(Opcode.SYSCALL, callNumner);
		return S();
	}

	public int start(int start)
	{
		this.start = start;
		return S();
	}

	public int size(int size)
	{
		this.size = size;
		return S();
	}

	public Program build()
	{
		int[] code = new int[ instructions.size() ];
		int j = 0;
		for (int i : instructions)
		{
			code[j++] = i;
		}

		return new Program(code, start, size);
	}
}
