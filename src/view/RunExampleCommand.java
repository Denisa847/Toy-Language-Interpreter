package view;

import view.Command;
import controller.Controller;

public class RunExampleCommand extends Command {
    private final Controller controller;
    public RunExampleCommand(String key, String description, Controller controller){
        super(key,description);
        this.controller=controller;
    }

    @Override
    public void execute(){
        try{
            controller.allSteps();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
