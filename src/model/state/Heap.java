package model.state;
import model.value.Value;
import java.util.HashMap;
import java.util.Map;

public class Heap implements HeapTable{
    private final Map<Integer, Value> heap=new HashMap<>();
    private int nextFree=1;

    public synchronized int allocate(Value value){
        heap.put(nextFree, value);
        return nextFree++;
    }

    public synchronized Value get(int address){
        return heap.get(address);
    }

    public synchronized void update(int address, Value value){
        heap.put(address,value);
    }

    public Map<Integer,Value> getContent(){
        return heap;
    }

    public void setContent(Map<Integer,Value> newHeap){
        heap.clear();
        heap.putAll(newHeap);
    }

    @Override
    public String toString(){
        return heap.toString();
    }
}
