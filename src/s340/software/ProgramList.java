/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package s340.software;

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
