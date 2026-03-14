package model.state;
import model.value.Value;
import java.util.Map;
import java.util.List;
import javafx.util.Pair;
import model.value.Value;
import java.util.HashMap;
import java.util.Map;

public class CountSemaphore implements ISemaphore{
    private final Map<Integer, Pair<Integer,List<Integer>>> countSemaphore;
    private int nextFree;

    public CountSemaphore(){
        this.countSemaphore=new HashMap<>();
        this.nextFree=1;
    }

    @Override
    public synchronized int allocate(Pair<Integer, List<Integer>> value){
        int index=nextFree;
        countSemaphore.put(index, value);
        nextFree++;
        return index;
    }

    @Override
    public synchronized Pair<Integer, List<Integer>> loockup(int index){
        return countSemaphore.get(index);
    }

    @Override
    public synchronized void update(int index, Pair<Integer,List<Integer>> value){
        countSemaphore.put(index,value);

    }

    @Override
    public synchronized boolean contains(int index){
        return countSemaphore.containsKey(index);

    }

    @Override
    public synchronized Map<Integer,Pair<Integer,List<Integer>>> getContent(){
        return countSemaphore;

    }

    @Override
    public String toString(){
        return countSemaphore.toString();
    }




}
