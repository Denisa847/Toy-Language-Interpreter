package model.statement;
import exceptions.ExpressionException;
import model.expression.Expression;
import exceptions.MyException;
import model.state.ProgramState;
import model.type.Type;
import model.type.RefType;
import model.value.Value;
import model.value.RefValue;
import model.state.MyIDictionary;

public class WriteHeapStatement implements Statement {
    private final String varName;
    private final Expression expression;

    public WriteHeapStatement(String varName,Expression expression){
        this.varName=varName;
        this.expression=expression;
    }

    @Override
    public ProgramState execute(ProgramState state){
        if(!state.symbolTable().isDefined(varName)){
            throw new ExpressionException("Variable"+ varName+ "is not defined");

        }

        Value varValue=state.symbolTable().getValue(varName);

        //varValue must be RefValue
        if(!(varValue instanceof RefValue refValue)){
            throw new RuntimeException("Variable "+ varName+ "must be RefType. ");

        }

        int address=refValue.getAddress();
        if(state.heap().get(address)==null){
            throw new RuntimeException("Invalid heap address: "+ address);
        }

        Value evaluated= expression.evaluate(state.symbolTable(), state.heap());
        Type locationType= refValue.getLocationType();
        if(!evaluated.getType().equals(locationType)){
            throw new RuntimeException("Type mismatch in heap write: expected "+
                    locationType+ " but go "+ evaluated.getType());
        }
        state.heap().update(address,evaluated);
        return null;
    }

    @Override
    public Statement copyStatement(){
        return new WriteHeapStatement(varName,expression);
    }


    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeVar=typeEnv.lookup(varName);
        Type typeExp=expression.typecheck(typeEnv);

        if(!(typeVar instanceof RefType refType))
            throw new MyException("Is not a RefType");

        if(!refType.getInner().equals(typeExp))
            throw new MyException("Type mismatch");

        return typeEnv;
    }
}
