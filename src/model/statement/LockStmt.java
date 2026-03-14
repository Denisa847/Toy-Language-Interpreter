package model.statement;

import exceptions.MyException;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public class LockStmt implements Statement{
    private final String var;

    public LockStmt(String var){
        this.var=var;
    }

    @Override
    public ProgramState execute(ProgramState state){
        if(!state.symbolTable().isDefined(var)){
            throw new MyException("Is nto defined");
        }

        Value value =state.symbolTable().getValue(var);
        if(!value.getType().equals(new IntType())){
            throw new MyException("Is not defined");
        }

        int nr=((IntegerValue) value).getValue();
        synchronized (state.lockTable()){
            if(!state.lockTable().contains(nr)){
                throw new MyException("This index does not exist");
            }
            int idLock=state.lockTable().get(nr);
            if(idLock==-1){
                state.lockTable().update(nr,state.getId());
            }
            else{
                state.executionStack().push(this);
            }
        }



        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        if(!typeEnv.lookup(var).equals(new IntType())){
            throw new MyException("Must be type int");
        }
        return typeEnv;
    }

    @Override
    public String toString(){
        return "lock( "+var+")";
    }
}
