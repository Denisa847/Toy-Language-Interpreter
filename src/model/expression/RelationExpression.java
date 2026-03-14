package model.expression;

import exceptions.ExpressionException;
import exceptions.MyException;
import model.state.SymbolTable;
import model.state.Heap;
import model.type.*;
import model.value.BooleanValue;
import model.value.*;
import model.state.MyIDictionary;

public record RelationExpression(String operator,Expression left, Expression right) implements Expression{
    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        Value leftValue=left.evaluate(symbolTable,heap);
        Value rightValue = right.evaluate(symbolTable, heap);

        // both operands must be integers
        if (!(leftValue.getType() instanceof IntType) || !(rightValue.getType() instanceof IntType)) {
            throw new ExpressionException("Relational expressions require integer operands.");
        }

        int n1 = ((IntegerValue) leftValue).value();
        int n2 = ((IntegerValue) rightValue).value();

        return switch (operator) {
            case "<" -> new BooleanValue(n1 < n2);
            case "<=" -> new BooleanValue(n1 <= n2);
            case "==" -> new BooleanValue(n1 == n2);
            case "!=" -> new BooleanValue(n1 != n2);
            case ">" -> new BooleanValue(n1 > n2);
            case ">=" -> new BooleanValue(n1 >= n2);
            default -> throw new ExpressionException("Unknown relational operator: " + operator);
        };
    }

    @Override
    public String toString() {
        return left + " " + operator + " " + right;
    }

    @Override
    public Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException {
        Type t1=left.typecheck(typeEnv);
        Type t2=right.typecheck(typeEnv);

        if(!t1.equals(new IntType()))
            throw new MyException("First operand is not an integer");

        if(!t2.equals(new IntType()))
            throw new MyException("Second operand is not an integer");
        return new BoolType();
    }

}
