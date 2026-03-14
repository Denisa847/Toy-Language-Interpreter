package model.type;
import model.value.*;

public interface Type {
    boolean equals(Object another);
    Value defaultValue();
    String toString();
}

