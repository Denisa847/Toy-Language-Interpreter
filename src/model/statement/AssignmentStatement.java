package model.statement;

import model.expression.Expression;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.state.SymbolTable;
import model.type.Type;
import model.value.Value;
import exceptions.*;
import exceptions.MyException;


public record AssignmentStatement(Expression expression, String variableName) implements Statement{
    @Override
    public ProgramState execute(ProgramState state){
        SymbolTable symbolTable=state.symbolTable();
        if(!symbolTable.isDefined(variableName)){
            throw new SymbolTableException("Variable not defined");
        }
        Value value=expression.evaluate(state.symbolTable(), state.heap());
        if(!value.getType().equals(symbolTable.getType(variableName))){
            throw new ExpressionException("Type mismatch");
        }
        symbolTable.update(variableName,value);
        return null;
    }

    @Override
    public Statement copyStatement(){ return new AssignmentStatement(expression,variableName);}


    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException{
        Type typevar=typeEnv.lookup(variableName);
        Type typexp=expression.typecheck(typeEnv);
        if(typevar.equals(typexp))
            return typeEnv;
        else
            throw new MyException("Assignment: right hand side and left hand side have different types");

    }


}