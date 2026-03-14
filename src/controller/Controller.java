package controller;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import model.state.ProgramState;
import model.statement.Statement;
import repository.Repository;
import exceptions.*;

//manages program execution through the Repository.
public class Controller {
    private final Repository repository;
    private ExecutorService executor;

    public Controller(Repository repository){
        this.repository=repository;
    }

    public List<ProgramState> getProgramList() {
        return repository.getPrgList();
    }

    private List<ProgramState> removeCompletedPrg(List<ProgramState> prgList){
        return prgList.stream().filter(ProgramState::isNotCompleted).collect(Collectors.toList());

    }

    public List<ProgramState> oneStepForAllPrg(List<ProgramState> prgList) throws MyException{
        prgList.forEach(prg->{
            try{
                repository.logPrgStateExec(prg);
            }catch (MyException e){
                throw new RuntimeException(e);
            }
        });

        List<Callable<ProgramState>> callList=prgList.stream()
                .map(p->(Callable<ProgramState>) p::oneStep)
                .collect(Collectors.toList());

        //start execution with threads
        List<ProgramState> newStates;
            try{
                newStates= executor.invokeAll(callList).stream()
                        .map(future->{
                            try{
                                return future.get();
                            }catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                        }).filter(p->p!=null)
                        .collect(Collectors.toList());

            }catch (InterruptedException e){
                throw new MyException("Thread execution interrupted");
            }


        prgList.addAll(newStates);
        prgList.forEach(prg->{
            try{
                repository.logPrgStateExec(prg);
            }catch(MyException e){
                throw new RuntimeException(e);
            }
        });

        return prgList;

    }

    public void oneStepGUI() throws MyException {
        executor = Executors.newFixedThreadPool(2);

        List<ProgramState> prgList = removeCompletedPrg(repository.getPrgList());
        if (prgList.isEmpty()) {
            executor.shutdownNow();
            throw new MyException("Program finished / no more states.");
        }

        runGarbageCollector(prgList);
        prgList = oneStepForAllPrg(prgList);
       // prgList = removeCompletedPrg(prgList);
        repository.setPrgList(prgList);

        executor.shutdownNow();
    }



    public void allSteps() throws MyException{
        executor= Executors.newFixedThreadPool(2);
        List<ProgramState> prgList=repository.getPrgList();
        prgList=removeCompletedPrg(prgList);

        while(!prgList.isEmpty()) {
            runGarbageCollector(prgList);
            prgList.forEach(prg->repository.logPrgStateExec(prg));

            prgList = oneStepForAllPrg(prgList);
            prgList = removeCompletedPrg(prgList);
        }
        executor.shutdownNow();
        repository.setPrgList(prgList);

    }

    private void runGarbageCollector(List<ProgramState> prgList){
        var heapConent=prgList.get(0).heap().getContent();

        var symTables=prgList.stream().flatMap(prg->prg.symbolTable().getContent().values().stream())
                .collect(Collectors.toList());

        var cleaned=GarbageCollector.safeGarbageCollector(symTables,heapConent);
        prgList.get(0).heap().setContent(cleaned);
    }


}

/*    public ProgramState oneStep(ProgramState state){
        var stack=state.executionStack();
        if(state.executionStack().isEmpty()){
            throw new StackException("Execution stack is empty");

        }

        Statement currentStatement=stack.pop();
        return currentStatement.execute(state);
    }*/