package model.state;

import java.util.List;

//a structure that collects all the values that the program “prints.”
public interface Out {
    void add(Object value);
    List<Object> getList();
}
