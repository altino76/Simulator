package s340.software.os;
/**
 *
 * @author Alberto Ortiz; Andrew S. Nelson; Nathan Mittenzwey
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import s340.hardware.DeviceControllerOperations;
import s340.hardware.IInterruptHandler;
import s340.hardware.ISystemCallHandler;
import s340.hardware.ITrapHandler;
import s340.hardware.InterruptRegisters;
import s340.hardware.Machine;
import s340.hardware.Trap;
import s340.hardware.exception.MemoryFault;
import s340.software.FreeSpace;
import s340.software.ProcessControlBlock;
import s340.software.ProcessState;
import static s340.software.os.SystemCall.READ_DISK;
import static s340.software.os.SystemCall.SBRK;
import static s340.software.os.SystemCall.WRITE_CONSOLE;
import static s340.software.os.SystemCall.WRITE_DISK;
/*
 * The operating system that controls the software running on the S340 CPU.
 *
 * The operating system acts as an interrupt handler, a system call handler, and
 * a trap handler.
 */
public class OperatingSystem implements IInterruptHandler, ISystemCallHandler, ITrapHandler, DeviceControllerOperations {
    // the machine on which we are running.
    private final Machine machine;
    private List<FreeSpace> freeSpaces;
    private static final int PROCESS_TABLE_SIZE = 10;
    private ProcessControlBlock[] processTable;
    private Queue<IORequest>[] queues;
    //Tracks the current process that was just running
    //this is used to calculate the next process to run and tells where to save registers in PCB List
    private int currentProcess;
    // private InterruptRegisters iRegister;
    /*
  * Create an operating system on the given machine.
     */
    public OperatingSystem(Machine machine) throws MemoryFault {
        this.freeSpaces = new ArrayList<FreeSpace>();
        this.freeSpaces.add(new FreeSpace(0, Machine.MEMORY_SIZE));
        this.queues = new Queue[machine.NUM_DEVICES];
        //this.ConsoleQueue = new Queue<LinkedList>();
        this.processTable = new ProcessControlBlock[PROCESS_TABLE_SIZE];
        this.currentProcess = 0;
        this.machine = machine;
        // this.iRegister = new InterruptRegisters(machine.NUM_DEVICES);
        //this initiates the queues
        for (int i = 0; i < machine.NUM_DEVICES; i++) {
            queues[i] = new LinkedList<IORequest>();
        }
        //creation of the wait program
        ProgramBuilder b1 = new ProgramBuilder();
        b1.start(0);    //not an actual instruction just sets start point
        b1.jmp(0);
        b1.end();
        Program waitProcess = b1.build();
        List<Program> programs = new LinkedList<Program>();
        programs.add(waitProcess);
        //puts waitProg in PCB at index 0 and stores code into memory
        schedule(programs);
    }
    private FreeSpace findFreeSpace(int size) {
        for (FreeSpace f : freeSpaces) {
            if (f.getLimit() >= size) {
                return f;
            }
        }
        return null;
    }
    //scans process table to choose next process to run
    private int chooseNextProcess() {
        //this.processTable[currentProcess].setStatus(ProcessState.READY);
        //Round Robbin form of scheduling
        //first scan from first process up 
        for (int i = currentProcess + 1; i < this.processTable.length; i++) {
            if (this.processTable[i] != null && this.processTable[i].getStatus() == ProcessState.READY) {
                this.processTable[i].setStatus(ProcessState.RUNNING);
                return i;
            }
        }
        //now scan from bottom to currentProcess
        for (int j = 1; j <= currentProcess; j++) {
            if (this.processTable[j] != null && this.processTable[j].getStatus() == ProcessState.READY) {
                this.processTable[j].setStatus(ProcessState.RUNNING);
                return j;
            }
        }
        //if no process is ready the wait process is chosen 
        this.processTable[0].setStatus(ProcessState.RUNNING);
        return 0;
    }
    /*
  * Load a program into a given memory address
     */
    private int loadProgram(Program program, int start) throws MemoryFault {
        //ensure the start adress is the start of the program
        int address = start;
        for (int i : program.getCode()) {
            machine.memory.store(address++, i);
        }
        return address;
    }
    /*
  * Scheduled a list of programs to be run.
  * 
  * 
  * @param programs the programs to schedule
     */
    public synchronized void schedule(List<Program> programs) throws MemoryFault {
        this.machine.memory.setBase(0);
        this.machine.memory.setLimit(Machine.MEMORY_SIZE);
        for (Program program : programs) {
            //start is used for the initial pc for each program
            //set up pcb for each program
            //loop through to find an open index
            for (int i = 0; i < processTable.length; i++) {
                if (processTable[i] == null || processTable[i].getStatus() == ProcessState.END) {
                    int programSize = program.getCode().length + program.getDataSize();
                    FreeSpace f = findFreeSpace(programSize); //(program.getDataSize() * 2) + program.getStart() = limit
                    loadProgram(program, f.getBase());
                    processTable[i] = new ProcessControlBlock(f.getBase(), programSize);
                    int newBase = f.getBase() + programSize;
//                    MemoryController memCtrl = new MemoryController(program.getDataSize()*2); //creates new mem controller with size of limit
//                    memCtrl.setBase(f.getBase());
                    f.setBase(newBase);
                    int newLimit = f.getLimit() - programSize;
                    if (newLimit == 0) {
                        freeSpaces.remove(f);
                    } else {
                        f.setLimit(newLimit);
                    }
                    //we need to also chanage base/limit of mem controller
                    break;
                }
            }
        }
//        //***The following in Schedule is to check to make sure that our scheduler works**
//        for (int i = 0; i < processTable.length; i++) {
//            if (processTable[i] != null) {
//                System.out.println(i + " " + processTable[i]);
//            }
//        }
        // leave this as the last line
        machine.cpu.runProg = true;
    }
    public void write_console(int acc) {
        //does each device control operation 
        this.machine.devices[Machine.CONSOLE].controlRegister.register[0] = DeviceControllerOperations.WRITE;
        this.machine.devices[Machine.CONSOLE].controlRegister.register[1] = acc;
        this.machine.devices[Machine.CONSOLE].controlRegister.latch();
    }
    public void write_disk(IORequest io) {
        //this.machine.devices[].controlRegister.register[0] = DeviceControllerOperations.WRITE;
        this.machine.memory.setBase(this.processTable[io.getProgramNumber()].getBase());
        this.machine.memory.setLimit(this.processTable[io.getProgramNumber()].getLimit());
        int myCurrentAcc = this.processTable[io.getProgramNumber()].getAcc();
        try {
            int myDeviceNumber = this.machine.memory.load(myCurrentAcc);
            int myPlatter = this.machine.memory.load(myCurrentAcc + 1);
            int myStart = this.machine.memory.load(myCurrentAcc + 2);
            int myLength = this.machine.memory.load(myCurrentAcc + 3);
//            System.out.println(myDeviceNumber);
//            System.out.println(myPlatter);
//            System.out.println(myStart);
//            System.out.println(myLength);
            //System.out.println(io.getDevOp());
            this.machine.devices[myDeviceNumber].controlRegister.register[0] = io.getDevOp();
            this.machine.devices[myDeviceNumber].controlRegister.register[1] = myPlatter;
            this.machine.devices[myDeviceNumber].controlRegister.register[2] = myStart;
            this.machine.devices[myDeviceNumber].controlRegister.register[3] = myLength;
            int myMemoryLocation = this.machine.memory.load(myCurrentAcc + 4);
            for (int i = 0; i < myLength; i++) {
                this.machine.devices[myDeviceNumber].buffer[i] = this.machine.memory.load(myMemoryLocation);
                myMemoryLocation++;
            }
            System.out.println(Arrays.toString(this.machine.devices[myDeviceNumber].buffer));
            this.machine.devices[myDeviceNumber].controlRegister.latch();
        } catch (MemoryFault ex) {
            Logger.getLogger(OperatingSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void read_disk(IORequest io) {
        this.machine.memory.setBase(this.processTable[io.getProgramNumber()].getBase());
        this.machine.memory.setLimit(this.processTable[io.getProgramNumber()].getLimit());
        int myCurrentAcc = this.processTable[io.getProgramNumber()].getAcc();
        try {
            int myDeviceNumber = this.machine.memory.load(myCurrentAcc);
            int myPlatter = this.machine.memory.load(myCurrentAcc + 1);
            int myStart = this.machine.memory.load(myCurrentAcc + 2);
            int myLength = this.machine.memory.load(myCurrentAcc + 3);
//            System.out.println(myDeviceNumber);
//            System.out.println(myPlatter);
//            System.out.println(myStart);
//            System.out.println(myLength);
            //System.out.println(io.getDevOp());
            this.machine.devices[myDeviceNumber].controlRegister.register[0] = io.getDevOp();
            this.machine.devices[myDeviceNumber].controlRegister.register[1] = myPlatter;
            this.machine.devices[myDeviceNumber].controlRegister.register[2] = myStart;
            this.machine.devices[myDeviceNumber].controlRegister.register[3] = myLength;
            int myMemoryLocation = this.machine.memory.load(myCurrentAcc + 4);
            this.machine.devices[myDeviceNumber].controlRegister.latch();
        } catch (MemoryFault ex) {
            Logger.getLogger(OperatingSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public int sbrk(int acc) {
        //main structure for the brokerage of sbrk()
        boolean keepGoing = true;
        while (keepGoing) {
            keepGoing = expand(acc);
            if (keepGoing == false) {
                return 0;
            }
            keepGoing = move(acc);
            if (keepGoing == false) {
                return 0;
            }
            keepGoing = compact(acc);
            if (keepGoing == false) {
                return 0;
            }
        }
        return 1;
    }
    public boolean expand(int acc) {
        for (FreeSpace f : freeSpaces) {
            //if the size of the freeSpace is >= to the size of the program plus what we need and its adjacent to said freespace
            if (f.getLimit() >= this.processTable[currentProcess].getLimit() + acc
                    && this.processTable[currentProcess].getBase() + this.processTable[currentProcess].getLimit() == f.getBase()) {
                this.processTable[currentProcess].setLimit(this.processTable[currentProcess].getLimit() + acc);
                f.setBase(f.getBase() + acc);
                f.setLimit(f.getLimit() - acc);
                return false;
            }
        }
        return true;
    }
    public boolean move(int acc) {
        //finds a free space big enough to accomodate the program with its expansion
        //System.out.println("We are test ing Move!");
        FreeSpace sufficientFree = this.findFreeSpace(this.processTable[currentProcess].getLimit() + acc);
        //checking if there is a free space
        if (sufficientFree == null) {
            return true;
        }
        //we move everything over
        for (int i = this.processTable[currentProcess].getBase();
                i < this.processTable[currentProcess].getBase() + this.processTable[currentProcess].getLimit();
                i++) {
            try {
                //loading and storing to siphon all our data over bit by bit
                int temp = sufficientFree.getBase();
                int loaded = machine.memory.load(i);
                machine.memory.store(temp, loaded);
                temp++;
            } catch (MemoryFault ex) {
                Logger.getLogger(OperatingSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
            FreeSpace f = new FreeSpace(this.processTable[currentProcess].getBase(),
                    this.processTable[currentProcess].getLimit());
            freeSpaces.add(f);
            int oldBase = sufficientFree.getBase();
            int oldLimit = sufficientFree.getLimit();
            //setting the new base and limit of the new program and freespace
            this.processTable[currentProcess].setBase(sufficientFree.getBase());
            this.processTable[currentProcess].setLimit(this.processTable[currentProcess].getLimit() + acc);
            sufficientFree.setBase(sufficientFree.getBase() + this.processTable[currentProcess].getLimit() + acc);
            sufficientFree.setLimit(sufficientFree.getLimit() - (this.processTable[currentProcess].getLimit() + acc));
            freeSpaces.add(new FreeSpace(oldBase, oldLimit));
        }
        //moved program
        //this.processTable[currentProcess].setBase(sufficientFree.getBase());
        return false;
        // sufficientFree.setBase(this.processTable[currentProcess]);
    }
    public void merge() {
        // Sorts freespaces by their bases
        Collections.sort(freeSpaces);
        Iterator myIt = freeSpaces.iterator();
        FreeSpace oldF = null;
        if (myIt.hasNext()) {
            oldF = (FreeSpace) myIt.next();
        }
        while (myIt.hasNext()) {
            FreeSpace newF = (FreeSpace) myIt.next();
            if (oldF.getBase() + oldF.getLimit() == newF.getBase()) {
                oldF.setLimit(newF.getLimit() + oldF.getLimit());
                myIt.remove();
            } else {
                newF = oldF;
            }
        }
        //Still need to delete new freespace
    }
    public boolean compact(int acc) {
        List<ProcessControlBlock> snapshot = new ArrayList<>();
        for (int i = 1; i < this.processTable.length; i++) {
            if (this.processTable[i].getStatus() != null && this.processTable[i].getStatus() != ProcessState.END) {
                snapshot.add(this.processTable[i]);
            }
        }
        ProcessControlBlock current = this.processTable[currentProcess];
        Collections.sort(snapshot);
        int leftMargin = 4; //the first address immediately after the Wait Process
        int RightMargin = this.machine.memory.getLimit() - 1;
        for (ProcessControlBlock P : snapshot) {
            // if P.getBase is less than the current process' base --> move left
            if (P.getBase() <= current.getBase()) {
                for (int i = P.getBase(); i < P.getBase() + P.getLimit(); i++) {
                    try {
                        int loaded = machine.memory.load(i);
                        machine.memory.store(leftMargin, loaded);
                        leftMargin++;
                    } catch (MemoryFault ex) {
                        Logger.getLogger(OperatingSystem.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                P.setBase(leftMargin - P.getLimit()); //this changes the base to the Left Start
            } else { //if P.getBase() < current base --> move right
                for (int i = P.getBase(); i < P.getBase() + P.getLimit(); i++) {
                    try {
                        int loaded = machine.memory.load(i);
                        machine.memory.store(RightMargin, loaded);
                        RightMargin--;
                    } catch (MemoryFault ex) {
                        Logger.getLogger(OperatingSystem.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                P.setBase(RightMargin);
            }
        }
        freeSpaces.removeAll(freeSpaces);
        freeSpaces.add(new FreeSpace(leftMargin, RightMargin - leftMargin));
        expand(this.processTable[currentProcess].getAcc());
        return true;
    }
    //method to save current cpu registers into the PCB
    private void saveRegisters(int pc) {
        this.processTable[currentProcess].setPc(pc);
        this.processTable[currentProcess].setAcc(this.machine.cpu.acc);
        this.processTable[currentProcess].setX(this.machine.cpu.x);
    }
    //method to restore cpu registers with PCB values
    private void restoreRegisters() {
        this.machine.cpu.setPc(this.processTable[currentProcess].getPc());
        this.machine.cpu.acc = this.processTable[currentProcess].getAcc();
        this.machine.cpu.x = this.processTable[currentProcess].getX();
        this.machine.memory.setBase(this.processTable[currentProcess].getBase());
        this.machine.memory.setLimit(this.processTable[currentProcess].getLimit());
//        if(currentProcess != 0)
//        {
//        System.err.println(this.machine.memory.getBase());
//        System.err.println(this.machine.memory.getLimit());
//        }
    }
    /*
    * Handle a trap from the hardware.
    * 
    * @param programCounter -- the program counter of the instruction after the
    * one that caused the trap.
    * 
    * @param trapNumber -- the trap number for this trap.
     */
    //At the moment four instructions will be ran between each trap call
    @Override
    public synchronized void trap(int savedProgramCounter, int trapNumber) {
        //  leave this code here
        CheckValid.trapNumber(trapNumber);
        if (!machine.cpu.runProg) {
            return;
        }
        //  end of code to leave
        //save registers
        this.saveRegisters(savedProgramCounter);
        switch (trapNumber) {
            case Trap.TIMER:
                //turn off clock //this is done for us
                //registers already saved
                this.processTable[currentProcess].setStatus(ProcessState.READY);
                //choose next program to run and update currentProcess var
                currentProcess = this.chooseNextProcess();
                break;
            case Trap.END:
                //set prog to finished and choose new prog
                //registers already saved
                this.processTable[this.currentProcess].setStatus(ProcessState.END);
                currentProcess = this.chooseNextProcess();
                //adds a freespace when the program ends
                FreeSpace newSpace = new FreeSpace(this.processTable[this.currentProcess].getBase(), this.processTable[this.currentProcess].getLimit());
                freeSpaces.add(newSpace);
                break;
            default:
                System.err.println("UNHANDLED TRAP " + trapNumber);
                System.exit(1);
        }
        this.restoreRegisters(); //restore pc,acc, and x reg
    }
    /*
  * Handle a system call from the software.
  * 
  * @param programCounter -- the program counter of the instruction after the
  * one that caused the trap.
  * 
  * @param callNumber -- the callNumber of the system call.
  * 
  * @param address -- the memory address of any parameters for the system
  * call.
     */
    @Override
    public synchronized void syscall(int savedProgramCounter, int callNumber) {
        //  leave this code here
        CheckValid.syscallNumber(callNumber);
        this.saveRegisters(savedProgramCounter);
        if (!machine.cpu.runProg) {
            return;
        }
        switch (callNumber) {
            case SBRK:
                sbrk(machine.cpu.acc);
                //we dont want to restore registers in WRITE_CONSOLE. That is done in interrupt handler
                //this.restoreRegisters();
                break;
            case WRITE_CONSOLE:
                this.processTable[currentProcess].setStatus(ProcessState.WAITING);
                // set next processs to running? this.processTable[currentProcess + 1].setStatus(ProcessState.RUNNING);
                if (queues[Machine.CONSOLE].isEmpty()) {
                    write_console(machine.cpu.acc);
                }
                queues[Machine.CONSOLE].add(new IORequest(currentProcess, DeviceControllerOperations.WRITE));
                currentProcess = this.chooseNextProcess();
                break;
            case WRITE_DISK:
                int tempAcc = this.processTable[currentProcess].getAcc();
                
                this.processTable[currentProcess].setStatus(ProcessState.WAITING);

                 {
                    try {
                        
                        IORequest io = new IORequest(currentProcess, DeviceControllerOperations.WRITE, this.machine.memory.load(tempAcc + 2), this.machine.memory.load(tempAcc + 3));

                       
                        int myDevice = machine.memory.load(tempAcc);
                        if (queues[myDevice].isEmpty()) {
                            //If queue is empty write to the disk
                            write_disk(io);
                        }
                        queues[myDevice].add(io);
                    } catch (MemoryFault ex) {
                        Logger.getLogger(OperatingSystem.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                currentProcess = this.chooseNextProcess();
                break;
            case READ_DISK:
                tempAcc = this.processTable[currentProcess].getAcc();
                this.processTable[currentProcess].setStatus(ProcessState.WAITING);
                //IORequest readIo = new IORequest(currentProcess, DeviceControllerOperations.READ);
                 {
                    try {
                        
                        IORequest readIo = new IORequest(currentProcess, DeviceControllerOperations.READ, this.machine.memory.load(tempAcc + 2), this.machine.memory.load(tempAcc + 3));
                        //int tempAcc = this.processTable[currentProcess].getAcc();
                        int myDevice = machine.memory.load(tempAcc);
                        if (queues[myDevice].isEmpty()) {
                            //If queue is empty write to the disk
                            read_disk(readIo);
                        }
                        queues[myDevice].add(readIo);
                    } catch (MemoryFault ex) {
                        Logger.getLogger(OperatingSystem.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                currentProcess = this.chooseNextProcess();
                break;
        }
        this.restoreRegisters();
       
        //  end of code to leave
    }
//    public synchronized void 
    /*
  * Handle an interrupt from the hardware.
  * 
  * @param programCounter -- the program counter of the instruction after the
  * one that caused the trap.
  * 
  * @param deviceNumber -- the device number that is interrupting.
     */
    @Override
    public synchronized void interrupt(int savedProgramCounter, int deviceNumber) {
        //  leave this code here
        CheckValid.deviceNumber(deviceNumber);
        if (!machine.cpu.runProg) {
            return;
        }
        //  end of code to leave
        saveRegisters(savedProgramCounter);
        machine.interruptRegisters.register[deviceNumber] = false;
        IORequest finished = queues[deviceNumber].remove();
        //gets the snapshot of where "I" was. currentProcess itself may change. Wakes program up
        this.processTable[finished.getProgramNumber()].setStatus(ProcessState.READY);
        switch (deviceNumber) {
            case Machine.CONSOLE:
                if (!(queues[machine.CONSOLE].isEmpty())) {
                    int currentNum = queues[Machine.CONSOLE].peek().getProgramNumber();
                    write_console(this.processTable[currentNum].getAcc());
                }
                break;
            case Machine.DISK1:
                if (finished.getDevOp() == DeviceControllerOperations.READ) {
                    try {
                        int newBase = this.processTable[finished.getProgramNumber()].getBase();
                        int newLimit = this.processTable[finished.getProgramNumber()].getLimit();
                        this.machine.memory.setBase(newBase);
                        this.machine.memory.setLimit(newLimit);
                        int parameterLocation = this.processTable[finished.getProgramNumber()].getAcc();
                        int myLength = this.machine.memory.load(parameterLocation + 3);
                        int myMemoryAddress = this.machine.memory.load(parameterLocation + 4);
                        for (int i = 0; i < myLength; i++) {
                            machine.memory.store(myMemoryAddress, this.machine.devices[Machine.DISK1].buffer[i]);
                            System.out.println(myMemoryAddress + ", " + this.machine.devices[Machine.DISK1].buffer[i]);
                            myMemoryAddress++;
                        }
                    } catch (MemoryFault ex) {
                        ex.printStackTrace(System.err);
                    }
                }
                //for(int i = 0; i < )
                //int myCurrentAcc = this.processTable[io.getCurrentI()].getAcc();
                if (!(queues[machine.DISK1].isEmpty())) {
                    queues[Machine.DISK1] = sstf(finished.getStartLocation() + finished.getpLength() - 1, (LinkedList) queues[Machine.DISK1]);
                    IORequest myRequest = queues[Machine.DISK1].peek();
                    System.out.println(myRequest);
                    if (myRequest.getDevOp() == DeviceControllerOperations.WRITE) {
                        write_disk(myRequest);
                    }
                    if (myRequest.getDevOp() == DeviceControllerOperations.READ) {
                        read_disk(myRequest);
                    }
                }
                break;
        }
        this.restoreRegisters();
    }
    
    public IORequest chooseNearestIo(int endPos, LinkedList<IORequest> deviceQueue)
    {
        int minDisplacement = Integer.MAX_VALUE;
        int pIndex = 0;
        
        for(int i = 0; i < deviceQueue.size(); i++)
        {
            int distance = Math.abs(endPos - deviceQueue.peek().getStartLocation());
            
            if(distance < minDisplacement)
            {
                minDisplacement = distance;
                pIndex = i;
            }
            
            
        }
        
        return deviceQueue.get(pIndex);
    }
    
    public LinkedList<IORequest> sstf(int armPos, Queue<IORequest> deviceQueue)
    {
        LinkedList<IORequest> copyDeviceQueue = (LinkedList) deviceQueue;
        
        IORequest pointer = chooseNearestIo(armPos, copyDeviceQueue);
        
        copyDeviceQueue.remove(pointer);
        
        copyDeviceQueue.add(0, pointer);
        
        return copyDeviceQueue;
        
        
        
    }
   
}