package model.state;
import model.statement.Statement;

import java.util.List;
import java.util.LinkedList;

//defines how the execution stack works
//it uses a LinkedList internally to simulate a stack
public class StackExecutionStack implements ExecutionStack {
    private final List<Statement> stack=new LinkedList<>();

    @Override
    public void push(Statement statement){
        stack.addFirst(statement);
    }

    @Override
    public Statement pop(){
        return stack.removeFirst();
    }

    @Override
    public boolean isEmpty(){
        return stack.isEmpty();
    }

    @Override
    public String toString(){
        return stack.toString();
    }

    @Override
    public String format(){
        StringBuilder stringBuilder=new StringBuilder("Execution Stack\n");
        for(Statement statement: stack){
            stringBuilder.append(statement).append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public List<Statement> getStack() {
        return stack;
    }
}
