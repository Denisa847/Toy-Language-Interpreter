package model.state;

import exceptions.MyException;
import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K,V> implements MyIDictionary<K,V> {
    private final Map<K,V> dictionary;

    public MyDictionary(){
        this.dictionary=new HashMap<>();
    }

    @Override
    public void put(K key, V value){
        dictionary.put(key,value);
    }

    @Override
    public V lookup(K key) throws MyException{
        if(!dictionary.containsKey(key))
            throw new MyException("key "+key+ "not found");
        return dictionary.get(key);
    }

    @Override
    public boolean isDefined(K key){
        return dictionary.containsKey(key);
    }

    @Override
    public void remove(K key){
        dictionary.remove(key);
    }

    @Override
    public Map<K,V> getContent(){
        return dictionary;
    }

    @Override
    public MyIDictionary<K,V> deepCopy(){
        MyIDictionary<K,V> copy=new MyDictionary<>();
        for(var entity: dictionary.entrySet()){
            copy.put(entity.getKey(),entity.getValue());
        }
        return copy;
    }

}
