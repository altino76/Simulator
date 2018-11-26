/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package s340.software.os;

import s340.software.ProcessControlBlock;

/**
 *
 * @author altino
 */
public class IORequest {
    //public ProcessControlBlock block;
    private int current;
    
//    public IORequest(ProcessControlBlock block)
//    {
//        this.block = block;
//    }
    public IORequest(int index)
    {
        current = index;
    }
    public int getCurrent(){
        return current;
    }
}
