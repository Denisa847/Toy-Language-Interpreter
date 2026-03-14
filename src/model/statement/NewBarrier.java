package model.statement;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;
import model.state.*;
import exceptions.MyException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewBarrier implements Statement {
    private final String var;
    private final Expression exp;

    public NewBarrier(String var, Expression exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState programState) {
        Value evaluat = exp.evaluate(programState.symbolTable(), programState.heap());
        if (!(evaluat instanceof IntegerValue intValue)) {
            throw new MyException("Wrong expression");
        }
        int nr = intValue.getValue();
        if (!programState.symbolTable().isDefined(var)) {
            throw new MyException("Variable " + var + " is not defined");
        }

        if (!programState.symbolTable().getType(var).equals(new IntType())) {
            throw new MyException("Variable " + var + "is not defined");
        }

        int newLocation;
        synchronized (programState.barrierTable()) {
            newLocation = programState.barrierTable().allocate(nr);
        }

        programState.symbolTable().update(var, new IntegerValue(newLocation));
        return null;

    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        if (!typeEnv.lookup(var).equals(new IntType())) {
            throw new MyException("The new variable must be int");
        }

        if (!exp.typecheck(typeEnv).equals(new IntType())) {
            throw new MyException("Wrong expression");
        }

        return typeEnv;

    }

    @Override
    public String toString() {
        return "newBarrier(" + var + "," + exp + ")";
    }

}
