package model.expression;

import exceptions.MyException;
import model.state.Heap;
import model.state.MyIDictionary;
import model.state.SymbolTable;
import model.type.BoolType;
import model.type.Type;
import model.value.BooleanValue;
import model.value.Value;

public class NotExpression implements Expression{
    private final Expression exp;

    public NotExpression(Expression exp){
        this.exp=exp;
    }

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {

        Value value = exp.evaluate(symbolTable, heap);
        if(!value.getType().equals(new BoolType())){
            throw new MyException("Must be int");
        }

        BooleanValue bv=(BooleanValue) value;
        return new BooleanValue(!bv.getValue());

    }

    @Override
    public Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException{
        if(!exp.typecheck(typeEnv).equals(new BoolType())){
            throw new MyException("Must be boolean");
        }
        return new BoolType();
    }


    @Override
    public String toString(){
        return "!("+exp+")";
    }


}
