package model.expression;
import exceptions.*;
import model.state.SymbolTable;
import model.state.Heap;
import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.Value;
import model.type.*;
import model.state. MyIDictionary;

//evaluates expressions with two operands and returns the resulting Value.
public record BinaryOperatorExpression
        (String operator, Expression left, Expression right)
        implements Expression {

    private Value evaluateArithmeticExpression(IntegerValue left, IntegerValue right){
        int n1=left.value();
        int n2=right.value();

        return switch(operator){
            case "+"->new IntegerValue(n1+n2);
            case "-"->new IntegerValue(n1-n2);
            case "*"->new IntegerValue(n1*n2);
            case "/"->{
                if( n2 == 0)
                    throw new ExpressionException("Division by zero");
                yield  new IntegerValue(n1/n2);
            }
            default -> throw new ExpressionException("Unknown operator: " + operator);
        };
    }

    private Value evaluateBooleanExpression(BooleanValue left,BooleanValue right){
        boolean b1=left.value();
        boolean b2=right.value();

        return switch(operator){
            case "&&"->new BooleanValue(b1&&b2);
            case "||"->new BooleanValue(b1||b2);
            default-> throw new ExpressionException("Invalid boolean operator "+operator);
        };
    }



    @Override
    public Value evaluate(SymbolTable symTable, Heap heap){
        Value leftTerm = left.evaluate(symTable,heap);
        Value rightTerm = right.evaluate(symTable, heap);

        switch (operator){
            case "+","-","*","/"-> {
                if(!(leftTerm.getType() instanceof IntType) || !(rightTerm.getType() instanceof IntType))
                     throw new ExpressionException("Arithmetic operations require int values");
                return evaluateArithmeticExpression((IntegerValue)leftTerm,(IntegerValue)rightTerm);
            }
            case "&&", "||"-> {
                if(!(leftTerm.getType() instanceof BoolType)|| !(rightTerm.getType() instanceof BoolType))
                    throw new ExpressionException("Boolean operations require bool vales");

                return evaluateBooleanExpression((BooleanValue) leftTerm, (BooleanValue) rightTerm);
            }
            default->throw new ExpressionException("Unknown operator: " +operator);
        }

    }

    @Override
    public Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException {
        Type t1 = left.typecheck(typeEnv);
        Type t2 = right.typecheck(typeEnv);

        switch (operator) {
            case "+", "-", "*", "/" -> {
                if (!t1.equals(new IntType()))
                    throw new MyException("First operand is not an integer");

                if (!t2.equals(new IntType()))
                    throw new MyException("Second operand is not an integer");

                return new IntType();
            }
            case "<", "<=", ">", ">=", "==", "!=" -> {
                if (!t1.equals(new IntType()) || !t2.equals(new IntType()))
                    throw new MyException("Operands must be integers for comparison");

                return new BoolType();
            }
            case "&&", "||" -> {
                if (!t1.equals(new BoolType()) || !t2.equals(new BoolType()))
                    throw new MyException("Operands must be boolean for logical operators");

                return new BoolType();
            }

            default -> throw new MyException("Unknown operator: " + operator);
        }
    }
}
