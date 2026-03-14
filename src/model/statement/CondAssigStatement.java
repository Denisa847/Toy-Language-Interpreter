package model.statement;

import exceptions.MyException;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.Type;
import  model.expression.Expression;

public class CondAssigStatement implements Statement{
    private final String v;
    private final Expression exp1;
    private final Expression exp2;
    private final Expression exp3;

    public CondAssigStatement(String v, Expression exp1,Expression exp2,Expression exp3){
        this.v=v;
        this.exp1=exp1;
        this.exp2=exp2;
        this.exp3=exp3;

    }



    @Override
    public ProgramState execute(ProgramState state){
        Statement expected=new IfStatement(exp1,
                new AssignmentStatement(exp2,v),
                new AssignmentStatement(exp3,v));

        state.executionStack().push(expected);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException{
        Type t1=exp1.typecheck(typeEnv);
        if(!t1.equals(new BoolType())){
            throw new MyException("Expr1 must be bool type");
        }

        Type t2=exp2.typecheck(typeEnv);
        Type t3=exp3.typecheck(typeEnv);
        Type typevar=typeEnv.lookup(v);

        if(!typevar.equals(t2) || !typevar.equals(t3)) {
            throw new MyException("Must have same type");
        }



        return typeEnv;
    }

    @Override
    public String toString(){
        return v+"= "+exp1+"?"+exp2+":"+exp3;
    }


}

