package model.statement;

import exceptions.MyException;
import model.state.*;
import model.type.Type;
import model.state.MyIDictionary;

public record ForkStatement(Statement statement) implements Statement{
    @Override
    public ProgramState execute(ProgramState state){
        StackExecutionStack newExecStack=new StackExecutionStack();
        newExecStack.push(statement);

        SymbolTable cloneSymTable=state.symbolTable().deepCopy();

        return new ProgramState(newExecStack ,cloneSymTable, state.out(), state.fileTable(),state.heap(), state.countSemaphore(), state.barrierTable(),state.lockTable(), state.countDownLatch());
    }

    @Override
    public String toString(){
        return "fork("+statement+")";
    }


    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
            statement.typecheck(typeEnv.deepCopy());
            return typeEnv;
    }

}
