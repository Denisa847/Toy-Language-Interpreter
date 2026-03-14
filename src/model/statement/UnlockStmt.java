package model.statement;

import exceptions.MyException;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public class UnlockStmt implements Statement{
    private final String var;
    public UnlockStmt(String var){
        this.var=var;
    }


    @Override
    public ProgramState execute(ProgramState state){
        if(!state.symbolTable().isDefined(var)){
            throw new MyException("Symbol table not defined");
        }

        Value value=state.symbolTable().getValue(var);
        if(!value.getType().equals(new IntType())){
            throw new MyException("Value type is not int");
        }

        int nr=((IntegerValue) value).getValue();
        synchronized (state.lockTable()){
            if(!state.lockTable().contains(nr)){
                throw new MyException("Lock table does not have this index");
            }
            int index=state.lockTable().get(nr);
            if(index==state.getId()){
                state.lockTable().update(nr,-1);

            }

        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        if (!typeEnv.lookup(var).equals(new IntType())) {
            throw new MyException("Must be int!");
        }

        return typeEnv;
    }

    @Override
    public String toString(){
        return "unlock( "+var+")";
    }
}
