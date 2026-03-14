package model.state;

import model.value.Value;
import model.type.Type;

import java.util.Map;
import java.util.HashMap;

//defines how those operations actually work
public class MapSymbolTable implements SymbolTable{
    private final Map<String, Value> map= new HashMap<>();

    @Override
    public boolean isDefined(String variableName) {
        return map.containsKey(variableName);
    }

    @Override
    public void declareVariable(String variableName, Type type){map.put(variableName,type.defaultValue());}

    @Override
    public void update(String variableName,Value value){ map.put(variableName, value);}

    @Override
    public Value getValue(String variableName){return map.get(variableName);}

    @Override
    public Type getType(String variableName){
        return map.get(variableName).getType();
    }

    @Override
    public String toString(){
        return map.toString();
    }

    @Override
    public SymbolTable deepCopy() {
        MapSymbolTable copy = new MapSymbolTable();
        for (var entry : map.entrySet()) {
            copy.map.put(entry.getKey(), entry.getValue().deepCopy());
        }
        return copy;
    }

    @Override
    public Map<String, Value> getContent() {
        return map;
    }

}


