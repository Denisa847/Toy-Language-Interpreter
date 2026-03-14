package model.state;

import javafx.util.Pair;
import java.util.Map;
import java.util.List;

public interface IBarrierTable {
    int allocate(int var1);
    Pair<Integer,List<Integer>> loockup(int index);
    void update(int index, Pair<Integer, List<Integer>> value);
    boolean contains(int index);
    Map<Integer, Pair<Integer, List<Integer>>> getContent();

}
