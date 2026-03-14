package model.state;

import model.statement.Statement;

import java.util.List;

//holds the sequence of statements that still have to be executed
//each statement is pushed when it's ready to run and poped when it's executed
public interface ExecutionStack {
    void push(Statement statement);

    Statement pop();

    boolean isEmpty();

    String format();

    List<Statement> getStack();
}
