package model.state;

import java.util.HashMap;
import java.util.Map;

public class CountDownTable implements CountDownLatch{
    private final Map<Integer, Integer> cntdownTable =new HashMap<>();
    private int nextFree=1;

    @Override
    public synchronized int allocate(int value){

        cntdownTable.put(nextFree, value);
        return nextFree++;

    }

    @Override
    public synchronized int get(int index){
        return cntdownTable.get(index);

    }

    @Override
    public synchronized void update(int index, int value){
        cntdownTable.put(index,value);
    }

    @Override
    public synchronized Map<Integer,Integer> getContent(){
        return cntdownTable;
    }

    @Override
    public synchronized boolean contains(int index){
        return cntdownTable.containsKey(index);
    }

    @Override
    public String toString(){
        return cntdownTable.toString();
    }
}
