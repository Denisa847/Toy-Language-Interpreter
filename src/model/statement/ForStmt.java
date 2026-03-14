package model.statement;
import exceptions.MyException;
import model.expression.Expression;
import model.expression.RelationExpression;
import model.expression.VariableExpression;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;


public class ForStmt implements Statement{
    private final String var;
    private final Expression exp1;
    private final Expression exp2;
    private final Expression exp3;
    private final Statement stmt;

    public ForStmt(String var, Expression exp1,Expression exp2, Expression exp3,Statement stmt){
        this.var=var;
        this.exp1=exp1;
        this.exp2=exp2;
        this.exp3=exp3;
        this.stmt=stmt;
    }


    @Override
    public ProgramState execute(ProgramState state){
        Statement expected=new CompoundStatement(
                new AssignmentStatement(exp1,var),
                new WhileStatement(new RelationExpression("<",new VariableExpression(var),exp2),
                        new CompoundStatement(stmt,new AssignmentStatement(exp3,var))

                )
        );
        state.executionStack().push(expected);
        return null;

    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException{
       if(!exp1.typecheck(typeEnv).equals(new IntType())){
           throw new MyException("Type must be int");
       }

       if(!exp2.typecheck(typeEnv).equals(new IntType())){
           throw new MyException("Type must be int");
       }

       if(!exp3.typecheck(typeEnv).equals(new IntType())){
           throw new MyException("Type must be int");
       }

        return typeEnv;
    }

    @Override
    public String toString(){
        return "for( "+ var+ "= "+ exp1 + "; "+
                var+"< " + exp2+"; "+
                var+"= "+ exp3+")" +stmt;
    }


}
