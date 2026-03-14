package model.statement;

import exceptions.ExpressionException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.BoolType;
import model.value.BooleanValue;
import model.value.Value;
import model.type.Type;
import exceptions.MyException;
import model.state.MyIDictionary;

public record WhileStatement(Expression condition, Statement body) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        Value condValue = condition.evaluate(state.symbolTable(), state.heap());

        if (!(condValue instanceof BooleanValue boolVal)) {
            throw new ExpressionException("Condition expression is not boolean");
        }

        if (boolVal.value()) {
            state.executionStack().push(this);
            state.executionStack().push(body);
        }
        return null;
    }

    @Override
    public Statement copyStatement() {
        return new WhileStatement(condition, body.copyStatement());
    }

    @Override
    public String toString() {
        return "while( " + condition + ") " + body;
    }


    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type condType=condition.typecheck(typeEnv);

        if(!condType.equals(new BoolType()))
            throw new MyException("Condition expression is not boolean");

        body.typecheck(typeEnv.deepCopy());

        return typeEnv;
    }
}
