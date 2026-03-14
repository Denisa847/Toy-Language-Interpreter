package model.statement;

import exceptions.MyException;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public class CountDownStmt implements Statement{
    private final String var;

    public CountDownStmt(String var){
        this.var=var;
    }

    @Override
    public ProgramState execute(ProgramState state){
        if(!state.symbolTable().isDefined(var)){
            throw new MyException("variable must be defined");
        }

        Value value=state.symbolTable().getValue(var);
        if(!value.getType().equals(new IntType())){
            throw new MyException("Must be int type");
        }

        int foundIndex=((IntegerValue) value).getValue();
        synchronized (state.countDownLatch()){
            if(!state.countDownLatch().contains(foundIndex)){
                throw new MyException("Must be in countDownLatch");
            }

            int nr=state.countDownLatch().get(foundIndex);
            if(nr>0){
                state.countDownLatch().update(foundIndex, nr-1);
            }
            int currentPrgId= state.getId();
            state.out().add(currentPrgId);
        }

        return null;
    }


    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException{
        if(!typeEnv.lookup(var).equals(new IntType())){
            throw new MyException("Must be type int!");
        }

        return typeEnv;
    }
    @Override
    public String toString(){
        return "countdown("+var+")";
    }



}
