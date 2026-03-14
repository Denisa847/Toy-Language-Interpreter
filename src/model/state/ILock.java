package model.state;
import model.value.Value;

import java.util.Map;

public interface ILock{
    int allocate(int value);
    int get(int index);
    void update(int index, int value);
    boolean contains(int index);
    Map<Integer,Integer> getContent();
}