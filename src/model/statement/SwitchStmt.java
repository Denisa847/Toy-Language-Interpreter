package model.statement;
import exceptions.MyException;
import model.expression.Expression;
import model.expression.RelationExpression;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.Type;

public class SwitchStmt implements Statement{
    private final Expression exp;
    private final Expression exp1;
    private final Statement stmt1;
    private final Expression exp2;
    private final Statement stmt2;
    private final Statement stmt3;

    public SwitchStmt(Expression exp,Expression exp1,Statement stmt1, Expression exp2,Statement stmt2,Statement stmt3){
        this.exp=exp;
        this.exp1=exp1;
        this.stmt1=stmt1;
        this.exp2=exp2;
        this.stmt2=stmt2;
        this.stmt3=stmt3;
    }

    @Override
    public ProgramState execute(ProgramState state){
        Statement expected=new IfStatement(new RelationExpression("==",exp,exp1),
                stmt1,
                new IfStatement(new RelationExpression("==", exp,exp2),
                        stmt2,
                        stmt3)
        );

        state.executionStack().push(expected);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException{
        Type t1=exp.typecheck(typeEnv);
        Type t2=exp1.typecheck(typeEnv);
        Type t3=exp2.typecheck(typeEnv);

        if(!(t3.equals(t1) && t2.equals(t1))){
            throw new MyException("Must have same type");
        }
        this.stmt1.typecheck(typeEnv.deepCopy());
        this.stmt2.typecheck(typeEnv.deepCopy());
        this.stmt3.typecheck(typeEnv.deepCopy());

        return typeEnv;
    }

    @Override
    public String toString(){
        return "switch("+exp+") (case"+ exp1+":"+stmt1+") case("+exp2+":"+stmt2+")"+"(default:"+stmt3+")";
    }
}
