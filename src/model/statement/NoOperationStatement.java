package model.statement;

import exceptions.MyException;
import model.state.MyIDictionary;
import model.state.Out;
import model.state.ProgramState;
import model.type.Type;
//a neutral placeholder wherever a statement is syntactically required
//but you don’t actually want to perform any action.

public class NoOperationStatement implements Statement{
    @Override
    public ProgramState execute(ProgramState state){ return null;}

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }

}
