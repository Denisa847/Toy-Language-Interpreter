package model.state;

import model.value.Value;
import model.type.Type;
import java.util.*;

//it keeps track of which variables exist in the program, what type they are,
// and what value they currently hold.
public interface SymbolTable {
    boolean isDefined(String variableName);

    Type getType(String variableName);

    void declareVariable(String variableName, Type type);

    void update(String variableName,Value value);

    Value getValue(String variableName);

    SymbolTable deepCopy();

    Map<String,Value> getContent();
}
