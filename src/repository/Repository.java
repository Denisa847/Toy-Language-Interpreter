package repository;

import model.state.ProgramState;
import  exceptions.MyException;
import java.io.IOException;
import java.util.List;

//The Repository interface defines how the interpreter stores and accesses one or more running programs
public interface Repository {
    void addProgram(ProgramState program);
    List<ProgramState> getPrgList();
    void setPrgList(List<ProgramState> list);

    //void logProgramState() throws IOException;
    void logPrgStateExec(ProgramState state) throws MyException;

}


