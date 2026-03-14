package model.statement;

import exceptions.MyException;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.Value;
import model.value.IntegerValue;

public class AwaitStmt implements Statement{
    private final String var;

    public AwaitStmt(String var){
        this.var=var;
    }

    @Override
    public ProgramState execute(ProgramState state){
        if(!state.symbolTable().isDefined(var)){
            throw new MyException("This variable is not defined in symbolTable");
        }

        Value value=state.symbolTable().getValue(var);
        if(!value.getType().equals(new IntType())){
            throw new MyException("Must be int");
        }

        int foundIndex=((IntegerValue) value).getValue();
        synchronized (state.countDownLatch()){
            if(!state.countDownLatch().contains(foundIndex)){
                throw new MyException("Must be int");
            }
            int nr=state.countDownLatch().get(foundIndex);
            if(nr!=0){
                state.executionStack().push(this);
            }
        }


        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException{
        if(!typeEnv.lookup(var).equals(new IntType())){
            throw new MyException("Type variable must be int");
        }

        return typeEnv;
    }

    @Override
    public String toString(){
        return "await("+var+")";
    }



}
