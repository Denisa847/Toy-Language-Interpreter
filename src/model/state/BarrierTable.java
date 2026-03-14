package model.state;
import java.util.ArrayList;
import java.util.Map;

import exceptions.MyException;
import javafx.util.Pair;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class BarrierTable implements IBarrierTable {
    private final Map< Integer, Pair<Integer, List<Integer>>> table=new HashMap<>();
    private int nextFree=1;



    @Override
    public boolean contains(int index){
        synchronized (this){
            return table.containsKey(index);
        }
    }

    @Override
    public int allocate(int expected){
        synchronized(this){
            table.put(nextFree, new Pair<>(expected, new ArrayList<>()));
            return nextFree++;
        }
    }

    @Override
    public Pair<Integer, List<Integer>> loockup(int index){
        synchronized (this){
            return table.get(index);
        }
    }

    @Override
    public void update(int index, Pair<Integer, List<Integer>> value){
        synchronized(this){
            table.put(index,value);
        }
    }


    @Override
    public Map<Integer, Pair<Integer, List<Integer>>> getContent(){
        synchronized(this){
            return table;
        }
    }

    @Override
    public String toString(){
        return table.toString();
    }


}
