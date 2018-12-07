/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package s340.software;
import s340.hardware.Device;
import s340.hardware.Machine;
import s340.software.os.Program;
import s340.software.os.ProgramBuilder;
import s340.software.os.SystemCall;
/**
 *
 *
 *
 * @author natha
 *
 */
public class ProgramList {
    public static Program p1() {
        System.out.println("we are in the programlist");
        ProgramBuilder b1 = new ProgramBuilder();
        b1.size(100);
        b1.loadi(18);
        b1.store(40);
        b1.syscall(SystemCall.WRITE_CONSOLE);
        b1.store(41);
        b1.syscall(SystemCall.WRITE_CONSOLE);
        b1.store(42);
        b1.syscall(SystemCall.WRITE_CONSOLE);
        b1.store(43);
        b1.syscall(SystemCall.WRITE_CONSOLE);
        b1.store(44);
//        b1.output();
//        System.out.println("The program before write Console");
//        b1.syscall(SystemCall.WRITE_CONSOLE);
//        System.out.println("The program after write Console");
        b1.end();
//        System.out.println("The program is able to end");
        Program p1 = b1.build();
//        System.out.println(p1);
        return p1;
    }
    public static Program test1(int value) {
        //Program is mostly blank, it exists and takes up space for testing purposes. Used in Proj 2
        ProgramBuilder b1 = new ProgramBuilder();
        b1.size(20);
        b1.loadi(value);
        for (int i = 40; i < 45; i++) {
            b1.store(i);
        }
        b1.load(40);
        b1.syscall(SystemCall.WRITE_CONSOLE);
        b1.load(41);
        b1.syscall(SystemCall.WRITE_CONSOLE);
        b1.load(42);
        b1.syscall(SystemCall.WRITE_CONSOLE);
        b1.load(43);
        b1.syscall(SystemCall.WRITE_CONSOLE);
        b1.load(44);
        b1.syscall(SystemCall.WRITE_CONSOLE);
        b1.end();
        Program p = b1.build();
        return p;
    }
//    public static Program test2() {
//        //Program is mostly blank, it exists and takes up space for testing purposes. Used in Proj 2
//        ProgramBuilder b1 = new ProgramBuilder();
//        b1.size(18);
//        b1.end();
//        Program p = b1.build();
//        return p;
//    }
//    public static Program Pm()
//    {
//        //Creates program that needs more memory. Used in Proj 2
//        ProgramBuilder b1 = new ProgramBuilder();
//        b1.size(20);
//        b1.loadi(100);
//        b1.syscall(SystemCall.SBRK);
//        b1.loadi(420);
//        b1.output();
//        b1.end();
//        
//        
//        Program p = b1.build();
//        return p;
//    }
    public static Program readDiskTest() {
        ProgramBuilder b3 = new ProgramBuilder();
        b3.size(200);
        int device = Machine.DISK1;
        int platter = 3;
        int start = 3;
        int length = 5;
        int memoryLocation = 60;
        int parameterLocation = 50;
        b3.loadi(device);
        b3.store(parameterLocation);
        b3.loadi(platter);
        b3.store(parameterLocation + 1);
        b3.loadi(start);
        b3.store(parameterLocation + 2);
        b3.loadi(length);
        b3.store(parameterLocation + 3);
        b3.loadi(memoryLocation);
        b3.store(parameterLocation + 4);
        b3.loadi(parameterLocation);
        b3.syscall(SystemCall.READ_DISK);
        b3.end();
        Program p = b3.build();
        return p;
    }
    public static Program diskTest() {
        ProgramBuilder b2 = new ProgramBuilder();
        b2.size(200);
        int device = Machine.DISK1;
        int platter = 3;
        int start = 3;
        int length = 5;
        int memoryLocation = 60;
        int stored = 100;
        int parameterLocation = 50;
        for (int i = memoryLocation; i < memoryLocation + length; i++) {
            b2.loadi(stored);
            b2.store(i);
            stored++;
        }
        b2.loadi(device);
        b2.store(parameterLocation);
        b2.loadi(platter);
        b2.store(parameterLocation + 1);
        b2.loadi(start);
        b2.store(parameterLocation + 2);
        b2.loadi(length);
        b2.store(parameterLocation + 3);
        b2.loadi(memoryLocation);
        b2.store(parameterLocation + 4);
        b2.loadi(parameterLocation);
        b2.syscall(SystemCall.WRITE_DISK);
        b2.end();
        Program p = b2.build();
        System.out.println(p);
        return p;
    }
}
