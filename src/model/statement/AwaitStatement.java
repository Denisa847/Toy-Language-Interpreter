package model.statement;
import model.state.MyIDictionary;
import model.type.IntType;
import model.value.IntegerValue;
import model.value.Value;
import model.state.ProgramState;
import exceptions.MyException;
import javafx.util.Pair;
import java.util.List;
import model.type.Type;

public class AwaitStatement implements Statement {
    private final String var;

    public AwaitStatement(String var){
        this.var=var;
    }

    @Override
    public ProgramState execute(ProgramState state){
        if (!state.symbolTable().isDefined(var)) {
            throw new MyException("Is not defined");
        }

        Value value=state.symbolTable().getValue(var);
        if(!(value instanceof IntegerValue)){
            throw new MyException("The value must be int");
        }

        int foundIndex=((IntegerValue) value).getValue();
        synchronized(state.barrierTable()){
            if (!state.barrierTable().contains(foundIndex)){
                throw new MyException("Barrier index not found");
            }

            var entry=state.barrierTable().loockup(foundIndex);
            int N1=entry.getKey();
            List<Integer> L1=entry.getValue();

            int NL=L1.size();
            if(N1>NL) {
                int currentId=state.getId();
                if(!L1.contains(currentId)){
                    L1.add(currentId);
                }
                state.executionStack().push(this);
            }
        }

        return null;
    }


    @Override
    public MyIDictionary<String,Type> typecheck(MyIDictionary<String,Type>typeEnv)throws MyException{
        if(!typeEnv.lookup(var).equals(new IntType())){
            throw new MyException("Await variable must be int");
        }

        return typeEnv;
    }

    @Override
    public String toString(){
        return "await("+var+") ";
    }



}
