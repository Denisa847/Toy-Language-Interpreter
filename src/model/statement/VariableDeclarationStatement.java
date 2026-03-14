package model.statement;

import exceptions.MyException;
import exceptions.SymbolTableException;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.Type;
//Adds new variable with default value
public record VariableDeclarationStatement(Type type,String variableName) implements Statement {

    @Override
    public ProgramState execute(ProgramState state){
        var symbolTable=state.symbolTable();
        if(symbolTable.isDefined(variableName)){
            throw new SymbolTableException("Variable already defined");
        }
        symbolTable.declareVariable(variableName,type);
        return null;
    }

    @Override
    public String format() {
        return type.toString() + " " + variableName;
    }

    @Override
    public MyIDictionary<String,Type> typecheck(MyIDictionary<String,Type> typeEnv) throws MyException{
        typeEnv.put(variableName, type);
        return typeEnv;
    }
}
