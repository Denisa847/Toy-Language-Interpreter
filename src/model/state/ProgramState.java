package model.state;

import exceptions.MyException;
import model.statement.Statement;


public class ProgramState {
    private static int lastId = 0;

    private static synchronized int getNewId() {
        lastId++;
        return lastId;
    }

    private final int id;

    private ExecutionStack executionStack;
    private SymbolTable symbolTable;
    private Out out;
    private FileTable fileTable;
    private Heap heap;
    private CountSemaphore countSemaphore;
    private BarrierTable barrierTable;
    private LockTable lockTable;
    private CountDownLatch countDownLatch;


    public ProgramState(ExecutionStack executionStack, SymbolTable symbolTable, Out out, FileTable fileTable, Heap heap,CountSemaphore countSemaphore, BarrierTable barrierTable,LockTable lockTable, CountDownLatch countDownLatch) {
        this.id = getNewId();
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.countSemaphore= countSemaphore;
        this.barrierTable=barrierTable;
        this.lockTable=lockTable;
        this.countDownLatch=countDownLatch;

    }

    public int getId() {
        return id;
    }

    public boolean isNotCompleted() {
        return !executionStack.isEmpty();
    }

    public ProgramState oneStep() throws MyException {
        if (executionStack.isEmpty())
            throw new MyException("ProgramState stack is empty");


        Statement statement = executionStack.pop();
        return statement.execute(this);
    }

    public ExecutionStack executionStack() {
        return executionStack;
    }

    public SymbolTable symbolTable() {
        return symbolTable;
    }

    public Out out() {
        return out;
    }

    public FileTable fileTable() {
        return fileTable;
    }

    public Heap heap() {
        return heap;
    }

    public CountSemaphore countSemaphore(){ return countSemaphore;}

    public BarrierTable barrierTable(){return barrierTable;}

    public LockTable lockTable(){
        return lockTable;
    }

    public CountDownLatch countDownLatch(){ return countDownLatch;}

    @Override
    public String toString() {
        return "ProgramState ID= " + id + "\n" +
                "Stack: " + executionStack.toString() + "\n" +
                "SymbolTable: " + symbolTable.toString() + "\n" +
                "Out: " + out() + "\n" +
                "FileTable: " + fileTable.toString() + "\n" +
                "Heap:" + heap.toString() + "\n"+
                "CountSemaphore:"+countSemaphore.toString() +"\n"+
                "BarrierTable: "+barrierTable.toString()+
                "LockTable:"+lockTable.toString()+
                "CountDownLatch" +countDownLatch.toString()+"\n";

    }



}

