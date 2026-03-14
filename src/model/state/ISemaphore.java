package model.state;
import model.value.Value;
import java.util.Map;
import java.util.List;
import javafx.util.Pair;
import model.value.Value;
import java.util.HashMap;
import java.util.Map;


public interface ISemaphore {
    int allocate(Pair<Integer, List<Integer>> value);
    Pair<Integer, List<Integer>> loockup(int index);
    void update(int index, Pair<Integer,List<Integer>> value);
    boolean contains(int index);
    Map<Integer,Pair<Integer,List<Integer>>> getContent();
}

