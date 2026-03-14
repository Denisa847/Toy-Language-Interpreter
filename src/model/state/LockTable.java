package model.state;

import java.util.Map;
import java.util.HashMap;


public class LockTable implements ILock{
    private final Map<Integer,Integer> lockTable;
    private int nextFree;

    public LockTable(){
        this.lockTable=new HashMap<>();
        this.nextFree=1;

    }

    public synchronized int allocate(int value){
        int index=nextFree;
        lockTable.put(index,value);
        nextFree++;
        return index;

    }
    public synchronized int get(int index){
        return lockTable.get(index);

    }

    public synchronized void update(int index, int value){
        lockTable.put(index,value);

    }

    public synchronized boolean contains(int index){
        return lockTable.containsKey(index);

    }

    public synchronized Map<Integer,Integer> getContent(){
        return lockTable;

    }

    @Override
    public String toString(){
        return lockTable.toString();
    }


}
