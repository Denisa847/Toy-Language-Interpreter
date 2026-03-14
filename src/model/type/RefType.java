package model.type;
import model.value.*;

public class RefType implements Type {
    private final Type inner;

    public RefType(Type inner){
        this.inner=inner;
    }

    public Type getInner(){
        return inner;
    }



    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof RefType other))
            return false;
        return inner.equals(other.inner);
    }

    @Override
    public Value defaultValue() {
        return new RefValue(0, inner);
    }

    @Override
    public String toString(){
        return "Ref("+inner+")";
    }

}


