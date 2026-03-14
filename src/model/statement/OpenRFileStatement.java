package model.statement;
import exceptions.*;
import model.expression.Expression;
import model.state.ProgramState;
import model.value.StringValue;
import model.state.FileTable;
import model.type.StringType;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import model.state.MyIDictionary;
import model.type.Type;

public record OpenRFileStatement(Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        var value = expression.evaluate(state.symbolTable(), state.heap());
        if (!(value instanceof StringValue(String fileName))) {
            throw new InvalidTypeException("Type must be String");
        }

        if (state.fileTable().isOpen(fileName)) {
            throw new FileAlreadyOpenException("File already open");
        }

        BufferedReader bufferReader;
        try {
            bufferReader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        state.fileTable().addOpenFile(fileName, bufferReader);
        return null;
    }


    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeexp = expression.typecheck(typeEnv);

        if (!typeexp.equals(new StringType()))
            throw new InvalidTypeException("Type must be string");

        return typeEnv;
    }
}