package model.value;

import model.type.Type;
import model.type.RefType;

public class RefValue implements Value {
    private final int address;
    private final Type locationType;

    public RefValue(int address, Type locationType){
        this.address=address;
        this.locationType=locationType;
    }

    public int getAddress(){
        return address;
    }

    public Type getLocationType(){
        return locationType;
    }

    @Override
    public Type getType(){
        return new RefType(locationType);
    }

    @Override
    public String toString(){
        return "("+ address+ ", " +locationType+")";
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof RefValue other)){
            return false;
        }

        return address==other.address &&
                locationType.equals(other.locationType);
    }

    @Override
    public Value deepCopy(){
        return new RefValue(address, locationType);
    }

}
