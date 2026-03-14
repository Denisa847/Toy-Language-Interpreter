package model.value;

import model.type.Type;
//it  allows to the toy language to actually hold and manipulate data during execution
public interface Value {
    Type getType();
    Value deepCopy();
}

