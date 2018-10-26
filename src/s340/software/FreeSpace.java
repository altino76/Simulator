/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package s340.software;

/**
 *
 * @author
 */
public class FreeSpace implements Comparable {

    private int base;
    private int limit;

    public FreeSpace(int base, int limit) {
        this.base = base;
        this.limit = limit;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public int compareTo(Object o) 
    {
        // Gets the base of the next freespace and sorts in ascending order.
        int oBase = ((FreeSpace) o).getBase();
        return this.base - oBase;
    }

}
