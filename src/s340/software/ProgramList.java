/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package s340.software;

import s340.hardware.Machine;
import s340.software.os.Program;
import s340.software.os.ProgramBuilder;
import s340.software.os.SystemCall;

/**
 *
 * @author natha
 */
public class ProgramList {

    public static Program p1() {
        //    System.out.println("we are in the programlist");

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
        b1.end();

        Program p1 = b1.build();
        return p1;
    }

    public static Program diskTest() {
        ProgramBuilder b2 = new ProgramBuilder();

        int device = Machine.DISK1;

        int platter = 3; //platter number

        int start = 31;//start location of platter

        int length = 20; //number of integers to be written

        int memoryLocation = 312; // where in memory we begin writing

        int parameterLocation = 300;

        int stored = 1;

        b2.size(1000);

        for (int i = 200; i < 221; i++) {
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

        b2.loadi(stored);
        b2.store(parameterLocation + 5);

        b2.loadi(parameterLocation);
        b2.syscall(SystemCall.WRITE_DISK);
        b2.end();

        Program p = b2.build();

        return p;

    }

    public static Program test1() {

        //Program is mostly blank, it exists and takes up space for testing purposes. Used in Proj 2
        ProgramBuilder b1 = new ProgramBuilder();
        b1.size(18);
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
}
