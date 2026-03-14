package model.statement;
import model.expression.Expression;
import model.state.ProgramState;
import model.state.SymbolTable;
import model.type.Type;
import model.state.MyIDictionary;
import exceptions.MyException;
import model.value.Value;
import model.type.IntType;
import model.state.Heap;
import model.value.IntegerValue;

public class newLatch implements Statement{
    private final String var;
    private final Expression expr;

    public newLatch(String var,Expression expr){
        this.var=var;
        this.expr=expr;
    }

    @Override
    public ProgramState execute(ProgramState state){
        Value value=expr.evaluate(state.symbolTable(),state.heap());
        if(!value.getType().equals(new IntType())){
            throw new MyException("Must be int");
        }

        int nr=((IntegerValue) value).getValue();
        synchronized (state.countDownLatch()){
            int newFreelocation=state.countDownLatch().allocate(nr);
            state.symbolTable().update(var, new IntegerValue(newFreelocation));
        }


        return null;
    }

    @Override
    public
    MyIDictionary<String,Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException{
        if(!typeEnv.lookup(var).equals(new IntType())){
            throw new MyException("Must be int");
        }

        if(!expr.typecheck(typeEnv).equals(new IntType())){
            throw new MyException("Must be int");
        }

        return typeEnv;
    }

    @Override
    public String toString(){
        return "newLatch("+var+", "+expr+")";
    }




}
