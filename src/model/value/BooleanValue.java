package model.value;
import model.type.BoolType;
import model.type.Type;

public record BooleanValue(boolean value) implements Value {
    @Override
    public Type getType() {
        return new BoolType();
    }

    @Override
    public Value deepCopy(){
        return new BooleanValue(this.value);
    }

    @Override
    public String toString() {
        return "" + value;
    }

    public boolean getValue(){
        return value;
    }
}

