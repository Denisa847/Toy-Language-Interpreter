package model.expression;

import model.state.SymbolTable;
import model.state.Heap;
import model.type.Type;
import model.value.Value;
import exceptions.*;
import model.state. MyIDictionary;
//it simply looks up the variable’s current value in the program’s symbol table
public record VariableExpression(String variableName) implements Expression {
    @Override
    public Value evaluate(SymbolTable symTable, Heap heap){
        if (!symTable.isDefined(variableName)) {
            throw new SymbolTableException("Variable not defined");
        }
        return symTable.getValue(variableName);
    }

    @Override
    public Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException{
        return typeEnv.lookup(variableName);
    }

}
