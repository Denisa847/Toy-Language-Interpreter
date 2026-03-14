package model.statement;

import exceptions.MyException;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public class newLock implements Statement{
    private final String var;

    public newLock(String var){
        this.var=var;
    }

    @Override
    public ProgramState execute(ProgramState state){
        if(!state.symbolTable().isDefined(var)){
            throw new MyException("Variable is not defined");
        }

        Value value=state.symbolTable().getValue(var);
        if(!value.getType().equals(new IntType())){
            throw new MyException("Value is not of type Int");
        }

        int newFreeLocation;
        int nr=((IntegerValue) value).getValue();
        synchronized (state.lockTable()){
            newFreeLocation=state.lockTable().allocate(-1);
        }
        state.symbolTable().update(var, new IntegerValue(newFreeLocation));

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException{
        if(!typeEnv.lookup(var).equals(new IntType())){
            throw new MyException("Must be int");
        }
        return typeEnv;
    }

    @Override
    public String toString(){
        return "newLock( "+var+")";
    }


}
