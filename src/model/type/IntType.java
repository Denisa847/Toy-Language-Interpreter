package model.type;
import model.value.*;

public class IntType implements Type{
    @Override
    public boolean equals(Object o){
        return o instanceof IntType;
    }

    @Override
    public Value defaultValue(){
        return new IntegerValue(0);

    }

    @Override
    public String toString(){
        return "int";
    }
}
