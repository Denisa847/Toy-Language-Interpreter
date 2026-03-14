package model.state;
import model.value.Value;

import java.util.Map;

public interface CountDownLatch{
    int allocate(int value);
    int get(int index);
    void update(int index, int value);
    Map<Integer,Integer> getContent();
    boolean contains(int index);
}
