package model.type;
import model.value.*;

public class BoolType implements Type{
    @Override
    public boolean equals(Object o){
        return o instanceof BoolType;
    }

    @Override
    public Value defaultValue(){
        return new BooleanValue(false);
    }

    @Override
    public String toString(){
        return "bool";
    }
}
