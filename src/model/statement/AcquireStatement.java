package model.statement;

import exceptions.MyException;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.Type;
import model.type.IntType;
import model.value.IntegerValue;
import model.value.Value;
import java.util.List;
import javafx.util.Pair;

public class AcquireStatement implements Statement{
    private final String var;

    public  AcquireStatement(String var){
        this.var=var;
    }

    @Override
    public ProgramState execute(ProgramState state){
        if (!state.symbolTable().isDefined(var)){
            throw new MyException("Variable nt defined");
        }

        Value value=state.symbolTable().getValue(var);
        if(!(value instanceof IntegerValue)){
            throw new MyException("value is not integer");
        }

        int foundIndex=((IntegerValue) value).getValue();
        synchronized(state.countSemaphore()){
            if(!state.countSemaphore().contains(foundIndex)){
                throw new MyException("Index not found");
            }

            Pair<Integer,List<Integer>> entry=state.countSemaphore().loockup(foundIndex);
            int N1=entry.getKey();
            List<Integer> list1=entry.getValue();
            int NL=list1.size();
            if(N1>NL){
                int currentIndex=state.getId();
                if(!state.countSemaphore().contains(currentIndex))
                    list1.add(currentIndex);

            }
            else{
                state.executionStack().push(this);
            }

        }


        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException{
        if(!(typeEnv.lookup(var).equals(new IntType()))){
            throw new MyException("Variable must be int");
        }
        return typeEnv;
    }

}