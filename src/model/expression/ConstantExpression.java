package model.expression;

import exceptions.MyException;
import model.state.SymbolTable;
import model.state.Heap;
import model.type.Type;
import model.value.Value;
import model.state.MyIDictionary;

//immutable wrapper for a constant and holds the actual runtime value
public record ConstantExpression(Value value) implements Expression {
    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap){
        return value;
    }

    @Override
    public Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException{
        return this.value.getType();
    }
}
