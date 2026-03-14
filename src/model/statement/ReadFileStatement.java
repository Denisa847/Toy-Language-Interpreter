package model.statement;

import exceptions.FileNotOpenException;
import exceptions.InvalidTypeException;
import model.expression.Expression;
import model.state.ProgramState;
import model.value.IntegerValue;
import model.value.StringValue;
import model.type.*;
import model.state.MyIDictionary;
import exceptions.MyException;

import java.io.BufferedReader;
import java.io.IOException;

public record ReadFileStatement(Expression expression, String varName) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {

        if (!state.symbolTable().isDefined(varName) ||
                !state.symbolTable().getType(varName).equals(new IntType())) {
            throw new InvalidTypeException(varName);
        }

        var value = expression.evaluate(state.symbolTable(), state.heap());
        if (!(value instanceof StringValue(String fileName))) {
            throw new InvalidTypeException("Type must be String");
        }

        if (!state.fileTable().isOpen(fileName)) {
            throw new FileNotOpenException(fileName);
        }

        BufferedReader br = state.fileTable().getOpenFile(fileName);
        String line;
        try {
            line = br.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        if (line == null) {
            state.symbolTable().update(varName, new IntegerValue(0));
        } else {
            state.symbolTable().update(varName, new IntegerValue(Integer.parseInt(line)));
        }
        return null;
    }


    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeVar= typeEnv.lookup(varName);

        if(!typeVar.equals(new IntType()))
            throw new MyException("Must be int ");

        Type typeExp=expression.typecheck(typeEnv);
        if(!typeExp.equals(new StringType()))
            throw new MyException("Must be String");

        return typeEnv;
    }
}