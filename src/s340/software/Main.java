package s340.software;

import java.util.LinkedList;
import java.util.List;

import s340.hardware.Machine;
import s340.software.os.OperatingSystem;
import s340.software.os.Program;


/**
 *
 * @author Alberto Ortiz; Andrew S. Nelson; Nathan Mittenzwey
 */
public class Main
{
    public static void main(String[] args) throws Exception
    {
    	//  setup the hardware, the operating system, and power up
        //	do not remove this
        Machine machine = new Machine();
        OperatingSystem os = new OperatingSystem(machine);
        machine.powerUp(os);
        // create a program

        //grab a program from collection, add to list, schedule list of programs
        List<Program> programs = new LinkedList<>();
        
        programs.add(ProgramList.p1());
        programs.add(ProgramList.p1());
        programs.add(ProgramList.p1());
             


        
        
       
        
       
        os.schedule(programs);
        System.out.println(programs);
        //System.out.println("base: " + machine.memory.getBase()+ " ; limit: " + machine.memory.getLimit());

    }
   
}
