package model.statement;

import exceptions.MyException;
import model.state.MyIDictionary;
import model.statement. Statement;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;

public class NewStatement implements Statement{
    private final String varName;
    private final Expression expression;

    public NewStatement(String varName, Expression expression){
        this.varName=varName;
        this.expression=expression;
    }

    @Override
    public ProgramState execute(ProgramState state) {

        if (!state.symbolTable().isDefined(varName)) {
            throw new RuntimeException("Variable not defined " + varName);
        }
        Value varValue = state.symbolTable().getValue(varName);

        if (!(varValue.getType() instanceof RefType refType))
            throw new RuntimeException("Variable " + varName + " must be RefType");

        //evaluate expression
        Value eval = expression.evaluate(state.symbolTable(), state.heap());
        Type innerType = refType.getInner();

        if (!eval.getType().equals(innerType))
            throw new RuntimeException("Type mismatch for new");

        //allocate the heap
        int address = state.heap().allocate(eval);

        state.symbolTable().update(varName, new RefValue(address, refType.getInner()));
        return null;
    }

    @Override
    public Statement copyStatement(){
        return new NewStatement(varName, expression);
        }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar=typeEnv.lookup(varName);
        Type typeexp=expression.typecheck(typeEnv);

        if(typevar.equals(new RefType(typeexp)))
            return typeEnv;
        else
            throw new MyException("Right hand side and left hand side have different types");
    }


}






