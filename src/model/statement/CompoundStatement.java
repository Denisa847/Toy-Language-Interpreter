package model.statement;


import exceptions.MyException;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.Type;

//represents a sequence of two statements that should be executed one after the other.
public record CompoundStatement(Statement first, Statement second ) implements Statement {
    @Override
    public ProgramState execute(ProgramState state){
        var stack=state.executionStack();
        stack.push(second);
        stack.push(first);
        return null;

    }

    @Override
    public Statement copyStatement(){return new CompoundStatement(first.copyStatement(),second.copyStatement());}

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException{
        return second.typecheck(first.typecheck(typeEnv));
    }


}



