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
    private int programNumber;
    private int devOp;
    private int startLocation;
    private int pLength;
    
    
    public IORequest(int index, int op)
    {
        programNumber = index;
        devOp = op;
    }
    //For disk scheduling
    public IORequest(int index, int op, int start, int length)
    {
        programNumber = index;
        devOp = op;
        startLocation = start;
        pLength = length;
    }
    public int getStartLocation() {
        return startLocation;
    }
    public int getpLength() {
        return pLength;
    }

    public int getProgramNumber() {
        return programNumber;
    }
    public int getDevOp() {
        return devOp;
    }
    @Override
    public String toString() {
        return "IORequest{" + "programNumber=" + programNumber + ", devOp=" + devOp + ", startLocation=" + startLocation + ", pLength=" + pLength + '}';
    }
    
    
}

