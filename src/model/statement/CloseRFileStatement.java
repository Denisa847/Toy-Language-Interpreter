package model.statement;

import exceptions.InvalidTypeException;
import exceptions.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.StringType;
import model.value.StringValue;
import model.state.MyIDictionary;
import model.type.Type;

public record CloseRFileStatement(Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        var value = expression.evaluate(state.symbolTable(), state.heap());
        if(!(value instanceof StringValue(String fileName))){
            throw new InvalidTypeException("Type must be String");
        }

        state.fileTable().closeFile(fileName);
        return null;

    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t=expression.typecheck(typeEnv);

        if(!t.equals(new StringType())){
            throw new MyException("Type must be string");
        }
        return typeEnv;
    }


}
