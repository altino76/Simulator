/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package s340.software;

/**
 *
 * @author Alberto Ortiz; Andrew S. Nelson; Nathan Mittenzwey
 */

public class ProcessControlBlock implements Comparable {
    private int acc;
    private int x;
    private int pc;
    private ProcessState status;
    private int base;
    private int limit;
    
    public ProcessControlBlock(int base, int limit) {
        //initial values for each program
        this.acc = 0;
        this.x = 0;
        this.pc = 0; //programmer identifies the starting point for each program
        this.status = ProcessState.READY;
        this.base = base;
        this.limit = limit;
    }

    public int getBase() {
        return base;
    }

    public int getLimit() {
        return limit;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    
    public int getAcc() {
        return acc;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public ProcessState getStatus() {
        return status;
    }

    public void setStatus(ProcessState status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProcessControlBlock{" + "acc=" + acc + ", x=" + x + ", pc=" + pc + ", status=" + status + ", base=" + base + ", limit=" + limit + '}';
    }

    @Override
    public int compareTo(Object o) {
        int oBase = ((ProcessControlBlock) o).getBase();
        return this.base - oBase;
    }
    
    
}


