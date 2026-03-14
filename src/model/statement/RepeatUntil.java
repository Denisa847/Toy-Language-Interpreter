package model.statement;

import exceptions.MyException;
import model.expression.NotExpression;
import model.expression.RelationExpression;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.Type;
import model.expression.Expression;


public class RepeatUntil implements Statement{
    private final Statement stmt;
    private final Expression exp;

    public RepeatUntil(Statement stmt,Expression exp){
        this.stmt=stmt;
        this.exp=exp;
    }

    @Override
    public ProgramState execute(ProgramState state){
        Statement expected=new CompoundStatement(
                stmt,
                new WhileStatement(new NotExpression(exp), stmt)
        );

        state.executionStack().push(expected);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        if(!exp.typecheck(typeEnv).equals(new BoolType())){
            throw new RuntimeException("Expression must be bool");
        }

        stmt.typecheck(typeEnv);

        return typeEnv;
    }

    @Override
    public String toString(){
        return "repeat "+stmt+" until("+exp+")";
    }
}
