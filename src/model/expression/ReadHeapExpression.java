package model.expression;
import model.state.Heap;
import model.state.SymbolTable;
import model.value.Value;
import model.value.RefValue;
import exceptions.MyException;
import model.type.*;
import model.state.MyIDictionary;

public class ReadHeapExpression implements Expression {
    private final Expression expression;

    public ReadHeapExpression(Expression expression){
        this.expression=expression;
    }

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap){
        Value value=expression.evaluate(symbolTable,heap);

        if(!(value instanceof RefValue refValue)){
            throw new RuntimeException("rH argument must be a RefValue");
        }

        int address=refValue.getAddress();
        Value heapValue=heap.get(address);

        if(heapValue==null){
            throw new RuntimeException("Invalid heap address"+address);
        }

        return heapValue;
    }

    @Override
    public String toString(){
        return "rH("+expression+")";
    }

    @Override
    public Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException {
        Type t= expression.typecheck(typeEnv);

        if(t instanceof RefType ref)
            return ref.getInner();
        throw new MyException("rH argument must be a RefType");
    }
}
