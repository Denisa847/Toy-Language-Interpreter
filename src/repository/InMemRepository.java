package repository;

import model.state.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import exceptions.*;

//InMemRepository keeps track of running programs in memory using a list
//it adds, retrieves, and manages ProgramState objects during execution.
public class InMemRepository implements Repository {
    private List<ProgramState> programs;  // = new ArrayList<>();
    private final String logFilePath;

    public InMemRepository(String logFilePath) {

        this.logFilePath = logFilePath;
        this.programs= new ArrayList<>();

        try {
            new PrintWriter(logFilePath).close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot clear log file: " + e.getMessage());
        }
    }

    /*@Override
    public ProgramState getCrtPrg() {
        if (programs.isEmpty()) {
            throw new RepositoryException("No programs available");
        }
        return programs.get(0);

    }*/

    @Override
    public void addProgram(ProgramState program) {
        programs.add(program);
    }

    @Override
    public List<ProgramState> getPrgList() {
        return programs;
    }


    @Override
    public void setPrgList(List<ProgramState> newList){
        this.programs=newList;
    }

    @Override
    public void logPrgStateExec(ProgramState state) throws MyException {
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {

            logFile.println(state.toString());
            logFile.println("\n");
        } catch (IOException e) {
            throw new MyException("Error writing to log file: " + e.getMessage());
        }
    }

}


