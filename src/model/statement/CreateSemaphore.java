package model.statement;
import java.sql.Array;
import java.util.Map;
import java.util.ArrayList;
import javafx.util.Pair;
import exceptions.MyException;
import model.expression.Expression;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.state.SymbolTable;
import model.type.IntType;
import model.type.Type;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import model.value.IntegerValue;
import model.value.Value;

public class CreateSemaphore implements Statement{
    private final String var;
    private final Expression exp;


    public CreateSemaphore(String var,Expression exp){
        this.var=var;
        this.exp=exp;
    }

    @Override
    public ProgramState execute(ProgramState state){
        if(!state.symbolTable().isDefined(var)){
            throw new MyException("Var not defined");
        }

        Value value=exp.evaluate(state.symbolTable(),state.heap());
        if(!(value instanceof IntegerValue)){
            throw new MyException("Value not Integer");
        }

        int number=((IntegerValue) value).getValue();
        int newLocation;

        synchronized(state.countSemaphore()){
            newLocation=state.countSemaphore().allocate(new Pair<>(number,new ArrayList<>()));
        }

        state.symbolTable().update(var,new IntegerValue(newLocation));
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException{
        if(!exp.typecheck(typeEnv).equals(new IntType())){
            throw new MyException("Must be int");
        }

        if(!(typeEnv.lookup(var).equals(new IntType()))){
            throw new MyException("Must be int");
        }
        return typeEnv;
    }

    @Override
    public String toString(){
        return "CreateSemaphore("+var+", "+exp+" )";
    }


}
