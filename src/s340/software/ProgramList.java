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
    
    
    
    public static Program p1(){
         
        ProgramBuilder b1 = new ProgramBuilder();
        b1.size(5); //program instruction goes to space 31; +5 units of data space to put stuff in
        
        //We reduced the number of 2's we're storing from 10 to 5 because we realized the assignment instructions 
        //called to store through locations 30 - 39 (which means that each "store 2" takes up 2 location spaces - "store" takes up 1 space; and " 2 " takes up another space
        //which means we can only store 2 five times within 10 spaces
        
        b1.loadi(2);    //loads 2 into accumulator
        
        int start = 26; 
        int end = 31;
        for(int i = start; i < end ; i ++) {
            b1.store(i); //this stores each value of 2 
           
        }
        b1.load(start); //sum = first value of 2
       // b1.output();
        for(int e = start + 1; e < end ;e++) {
            b1.add(e); //sum = sum + 2 --> b1 adds the value from location e
          //  b1.output();
        }
        
        b1.output(); //this prints out the sum
        
        b1.syscall(SystemCall.SBRK);
        b1.end();
        
        Program p1 = b1.build();
        System.out.println(p1);
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
    public static Program test2() {
        //Program is mostly blank, it exists and takes up space for testing purposes. Used in Proj 2
        ProgramBuilder b1 = new ProgramBuilder();
        b1.size(18);
        b1.end();
        Program p = b1.build();
        return p;
    }
    public static Program Pm()
    {
        //Creates program that needs more memory. Used in Proj 2
        ProgramBuilder b1 = new ProgramBuilder();
        b1.size(20);
        b1.loadi(100);
        b1.syscall(SystemCall.SBRK);
        b1.loadi(420);
        b1.output();
        b1.end();
        
        
        Program p = b1.build();
        return p;
    }
}