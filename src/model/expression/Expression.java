package model.expression;

import exceptions.MyException;
import model.state.SymbolTable;
import model.value.Value;
import model.state.Heap;
import model.state.MyIDictionary;
import model.type.Type;

//defines how to compute the result of an expression in the context of the current program state
public interface Expression {
    Value evaluate(SymbolTable symbolTable, Heap heap);

    Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException;
}