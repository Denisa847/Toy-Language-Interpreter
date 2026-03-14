package model.statement;

import model.expression.Expression;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.Type;
import model.type.BoolType;
import model.value.BooleanValue;
import model.value.Value;
import exceptions.*;

//The IfStatement checks a condition and decides which statement should run next.
//It doesn’t execute either branch itself, it just pushes the correct one onto the execution stack for the interpreter to handle later.
public record IfStatement(Expression condition,Statement thenBranch, Statement elseBranch) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        Value result=condition.evaluate(state.symbolTable(), state.heap());
        if( result instanceof BooleanValue booleanValue){
            if(booleanValue.value()){
                state.executionStack().push(thenBranch);
            }
            else {
                state.executionStack().push(elseBranch);
            }
        }
        else {
            throw new ExpressionException("Condition expression does not evaluate the boolean");
        }

       return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException{
        Type typeexp=condition.typecheck(typeEnv);
        if(typeexp.equals(new BoolType())) {
            thenBranch.typecheck(typeEnv);
            elseBranch.typecheck(typeEnv);
            return typeEnv;
        }
        else{
                throw new MyException("Condition expression does not evaluate the boolean");
        }
   }
}
