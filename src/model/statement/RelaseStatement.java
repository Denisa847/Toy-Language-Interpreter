package model.statement;
import exceptions.MyException;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.value.IntegerValue;
import model.type.Type;
import model.type.IntType;
import model.value.IntegerValue;
import model.value.Value;
import java.util.List;
import javafx.util.Pair;

public class RelaseStatement implements Statement {
    final private String var;

    public RelaseStatement(String var){
        this.var=var;
    }

    @Override
    public ProgramState execute(ProgramState state){
        if(!state.symbolTable().isDefined(var)){
            throw new MyException("Variable is not defined");
        }

        Value value=state.symbolTable().getValue(var);
        if(!(value instanceof IntegerValue)){
            throw new MyException("Variable is not defined");
        }

        int foundIndex=((IntegerValue) value).getValue();
        synchronized (state.countSemaphore()){
            if(!state.countSemaphore().contains(foundIndex)){
                throw new MyException("Must have index");
            }

            Pair<Integer, List<Integer>> entity=state.countSemaphore().loockup(foundIndex);
            int N1=entity.getKey();
            List<Integer> list1=entity.getValue();
            int currentId=state.getId();
            list1.remove(Integer.valueOf(currentId));
        }



        return null;
    }

    @Override
    public  MyIDictionary<String,Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException{
        if(!typeEnv.lookup(var).equals(new IntType())){
            throw new MyException("Variable must have int type");
        }

        return typeEnv;
    }

}
