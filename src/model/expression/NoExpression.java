package model.expression;
import model.type.BoolType;
import model.value.BooleanValue;
import exceptions.MyException;
import model.expression.Expression;
import model.state.Heap;
import model.state.MyIDictionary;
import model.state.SymbolTable;
import model.type.Type;
import model.value.Value;

public class NoExpression implements Expression{
    private final Expression expr;

    public NoExpression(Expression expr){
        this.expr=expr;
    }

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap){
        Value v=expr.evaluate(symbolTable, heap);
        if(!(v instanceof BooleanValue)){
            throw new MyException("Invalid expression");
        }
        BooleanValue bv=(BooleanValue) v;
        return new BooleanValue(! bv.getValue());


    }

    @Override
    public Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException{
        Type t= expr.typecheck(typeEnv);
        if(!t.equals(new BoolType())){
            throw new MyException("Must be boolean");

        }
        return new BoolType();
    }

    @Override
    public String toString(){
        return "!("+expr+")";
    }

}
