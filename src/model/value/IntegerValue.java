package model.value;
import model.type.IntType;
import model.type.Type;
public record IntegerValue(int value) implements Value {

    @Override
    public Type getType() {
        return new IntType();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        IntegerValue that = (IntegerValue) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public Value deepCopy(){
        return new IntegerValue(this.value);
    }

    @Override
    public String toString() {
        return "" + value;
    }

    public int getValue(){return value;}
}




