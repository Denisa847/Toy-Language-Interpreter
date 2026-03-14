package model.state;
import exceptions.MyException;
import java.util.Map;

public interface MyIDictionary<K,V> {
    void put(K hey, V value);

    V lookup(K key)throws MyException;

    boolean isDefined(K key);

    void remove(K key);

    Map<K,V> getContent();

    MyIDictionary<K,V> deepCopy();
}
