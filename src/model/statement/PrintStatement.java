package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.Type;

import java.io.BufferedReader;
import java.io.FileReader;

//It evaluates the expression and then sends the result to the Out object in the ProgramState.
public record PrintStatement(Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state){
        state.out().add(expression.evaluate(state.symbolTable(), state.heap()));
        return null;
    }


    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        expression.typecheck(typeEnv);
        return typeEnv;
    }
}
