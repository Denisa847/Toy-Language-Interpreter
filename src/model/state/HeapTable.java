package model.state;

import model.value.Value;
import java.util.Map;

public interface HeapTable {
    int allocate(Value value);
    Value get(int address);
    void update(int address, Value value);
    Map<Integer,Value> getContent();
    void setContent(Map<Integer,Value> newHeap);
}
