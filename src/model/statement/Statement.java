package model.statement;
import exceptions.MyException;
import model.state.ProgramState;
import model.type.Type;
import model.state.MyIDictionary;
//The Statement interface defines the blueprint for all statements
//defines how the statement behaves when it’s executed.
public interface Statement {
    ProgramState execute(ProgramState state);
    default Statement copyStatement(){ return this;}
    default String format(){return this.toString();}

    MyIDictionary<String,Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException;

}


