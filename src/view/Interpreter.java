//package view;
//
//import controller.Controller;
//import exceptions.MyException;
//import repository.Repository;
//import repository.InMemRepository;
//import model.expression.*;
//import model.state.*;
//import model.statement.*;
//import model.type.*;
//import model.value.*;
//import model.expression.RelationExpression;
//import java.util.Scanner;
//import model.state.MyIDictionary;
//
//public class Interpreter{
//    public static void typecheckProgram(Statement program){
//        MyIDictionary<String, Type> typeEnv=new MyDictionary<>();
//        try{
//            program.typecheck(typeEnv);
//        } catch (MyException e) {
//            System.out.println("Typecheck correctly failed: " + e.getMessage());
//        }
//    }
//
//    public static void main(String[] args) {
//        Statement ex1 = new CompoundStatement(
//                new VariableDeclarationStatement(new IntType(), "v"),
//                new CompoundStatement(new AssignmentStatement(new ConstantExpression(new IntegerValue(2)), "v"),
//                        new PrintStatement(new VariableExpression("v"))
//                )
//        );
//
//        Statement ex2 = new CompoundStatement(
//                new VariableDeclarationStatement(new IntType(), "a"),
//                new CompoundStatement(new VariableDeclarationStatement(new IntType(), "b"),
//                        new CompoundStatement(new AssignmentStatement(new BinaryOperatorExpression("+",
//                                new ConstantExpression(new IntegerValue(2)), new BinaryOperatorExpression("*",
//                                new ConstantExpression(new IntegerValue(3)),
//                                new ConstantExpression(new IntegerValue(5)))),
//                                "a"), new CompoundStatement(new AssignmentStatement(new BinaryOperatorExpression("+",
//                                new VariableExpression("a"), new ConstantExpression(new IntegerValue(1))
//                        ), "b"),
//                                new PrintStatement(new VariableExpression("b"))
//                        )
//                        )
//                )
//        );
//
//
//        Statement ex3 = new CompoundStatement(new VariableDeclarationStatement(new BoolType(), "a"),
//                new CompoundStatement(new VariableDeclarationStatement(new IntType(), "v"),
//                        new CompoundStatement(
//                                new AssignmentStatement(new ConstantExpression(new BooleanValue(true)), "a"),
//                                new CompoundStatement(new IfStatement(new VariableExpression("a"),
//                                        new AssignmentStatement(new ConstantExpression(new IntegerValue(2)), "v"),
//                                        new AssignmentStatement(new ConstantExpression(new IntegerValue(3)), "v")
//                                ),
//                                        new PrintStatement(new VariableExpression("v"))
//                                )
//                        )
//                )
//        );
//
//        Statement ex4 = new CompoundStatement(
//                new VariableDeclarationStatement(new StringType(), "varf"),
//                new CompoundStatement(
//                        new AssignmentStatement(
//                                new ConstantExpression(new StringValue("test.in")),
//                                "varf"
//                        ),
//                        new CompoundStatement(
//                                new OpenRFileStatement(new VariableExpression("varf")),
//                                new CompoundStatement(
//                                        new VariableDeclarationStatement(new IntType(), "varc"),
//                                        new CompoundStatement(
//                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
//                                                new CompoundStatement(
//                                                        new PrintStatement(new VariableExpression("varc")),
//                                                        new CompoundStatement(
//                                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
//                                                                new CompoundStatement(
//                                                                        new PrintStatement(new VariableExpression("varc")),
//                                                                        new CloseRFileStatement(new VariableExpression("varf"))
//                                                                )
//                                                        )
//                                                )
//                                        )
//                                )
//                        )
//                )
//        );
//
//        Statement ex5 = new CompoundStatement(
//                        new VariableDeclarationStatement(new RefType(new IntType()), "v"),
//                        new CompoundStatement(
//                                new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
//                                new CompoundStatement(
//                                        // FIX HERE: a : Ref Ref int
//                                        new VariableDeclarationStatement(new RefType(new RefType(new IntType())), "a"),
//                                        new CompoundStatement(
//                                                new NewStatement("a", new VariableExpression("v")),
//                                                new CompoundStatement(
//                                                        new PrintStatement(new VariableExpression("v")),
//                                                        new PrintStatement(new VariableExpression("a"))
//                                                )
//                                        )
//                                )
//                        )
//                );
//
//
//
//        Statement ex6 = new CompoundStatement(
//                        new VariableDeclarationStatement(new RefType(new IntType()), "v"),
//                        new CompoundStatement(
//                                new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
//                                new CompoundStatement(
//                                        new VariableDeclarationStatement(new RefType(new RefType(new IntType())), "a"),
//                                        new CompoundStatement(
//                                                new NewStatement("a", new VariableExpression("v")),
//                                                new CompoundStatement(
//                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
//                                                        new PrintStatement(
//                                                                new BinaryOperatorExpression("+",
//                                                                        new ReadHeapExpression(
//                                                                                new ReadHeapExpression(new VariableExpression("a"))
//                                                                        ),
//                                                                        new ConstantExpression(new IntegerValue(5))
//                                                                )
//                                                        )
//                                                )
//                                        )
//                                )
//                        )
//                );
//
//
//        Statement ex7 =
//                new CompoundStatement(
//                        new VariableDeclarationStatement(new RefType(new IntType()), "v"),
//                        new CompoundStatement(
//                                new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
//                                new CompoundStatement(
//                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
//                                        new CompoundStatement(
//                                                new WriteHeapStatement(
//                                                        "v",
//                                                        new ConstantExpression(new IntegerValue(30))
//                                                ),
//                                                new PrintStatement(
//                                                        new BinaryOperatorExpression(
//                                                                "+",
//                                                                new ReadHeapExpression(new VariableExpression("v")),
//                                                                new ConstantExpression(new IntegerValue(5))
//                                                        )
//                                                )
//                                        )
//                                )
//                        )
//                );
//
//        Statement ex8 = new CompoundStatement(
//                        new VariableDeclarationStatement(new RefType(new IntType()), "v"),
//                        new CompoundStatement(
//                                new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
//                                new CompoundStatement(
//                                        new VariableDeclarationStatement(new RefType(new RefType(new IntType())), "a"),
//                                        new CompoundStatement(
//                                                new NewStatement("a", new VariableExpression("v")),
//                                                new CompoundStatement(
//                                                        new NewStatement("v", new ConstantExpression(new IntegerValue(30))),
//                                                        new PrintStatement(
//                                                                new ReadHeapExpression(
//                                                                        new ReadHeapExpression(
//                                                                                new VariableExpression("a")
//                                                                        )
//                                                                )
//                                                        )
//                                                )
//                                        )
//                                )
//                        )
//                );
//
//        Statement ex9= new CompoundStatement(
//                        new VariableDeclarationStatement(new IntType(), "v"),
//                        new CompoundStatement(
//                                new AssignmentStatement(
//                                        new ConstantExpression(new IntegerValue(4)),
//                                        "v"
//                                ),
//                                new CompoundStatement(
//                                        new WhileStatement(
//                                                new RelationExpression(
//                                                        ">",
//                                                        new VariableExpression("v"),
//                                                        new ConstantExpression(new IntegerValue(0))
//                                                ),
//                                                new CompoundStatement(
//                                                        new PrintStatement(new VariableExpression("v")),
//                                                        new AssignmentStatement(
//                                                                new BinaryOperatorExpression(
//                                                                        "-",
//                                                                        new VariableExpression("v"),
//                                                                        new ConstantExpression(new IntegerValue(1))
//                                                                ),
//                                                                "v"
//                                                        )
//                                                )
//                                        ),
//                                        new PrintStatement(new VariableExpression("v"))
//                                )
//                        )
//                );
//
//        Statement ex10=
//                new CompoundStatement(
//                        new VariableDeclarationStatement(new IntType(), "v"),
//                        new CompoundStatement(
//                                new AssignmentStatement(new ConstantExpression(new IntegerValue(10)), "v"),
//                                new CompoundStatement(
//                                        new VariableDeclarationStatement(new RefType(new IntType()), "a"),
//                                        new CompoundStatement(
//                                                new NewStatement("a", new ConstantExpression(new IntegerValue(22))),
//                                                new CompoundStatement(
//                                                        new ForkStatement(
//                                                                new CompoundStatement(
//                                                                        new WriteHeapStatement("a", new ConstantExpression(new IntegerValue(30))),
//                                                                        new CompoundStatement(
//                                                                                new AssignmentStatement(new ConstantExpression(new IntegerValue(32)), "v"),
//                                                                                new CompoundStatement(
//                                                                                        new PrintStatement(new VariableExpression("v")),
//                                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
//                                                                                )
//                                                                        )
//                                                                )
//                                                        ),
//                                                        new CompoundStatement(
//                                                                new PrintStatement(new VariableExpression("v")),
//                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
//                                                        )
//                                                )
//                                        )
//                                )
//                        )
//                );
//
//
//        // typecheck
//        System.out.println("\n--- Typecheck demo (should FAIL) ---");
//
//        Statement badExample =
//                new CompoundStatement(
//                        new VariableDeclarationStatement(new IntType(), "x"),
//                        new AssignmentStatement(
//                                new ConstantExpression(new BooleanValue(true)),
//                                "x"
//                        )
//                );
//
//        try {
//            typecheckProgram(badExample);
//            System.out.println("ERROR: Typechecker did NOT detect the error!");
//        } catch (RuntimeException e) {
//            System.out.println("Typecheck correctly failed: " + e.getMessage());
//
//        }
//        typecheckProgram(badExample);
//
//
//        /*Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter log file name (e.g., log1.txt): ");
//        String logFilePath = scanner.nextLine();*/
//        typecheckProgram(ex1);
//        Repository repo1 = new InMemRepository("log1.txt");
//        ExecutionStack stack1 = new StackExecutionStack();
//        SymbolTable symTable1 = new MapSymbolTable();
//        Out out1 = new ListOut();
//        FileTable fileTable1 = new MapFileTable();
//        Heap heap1=new Heap();
//        ProgramState prg1 = new ProgramState(stack1, symTable1, out1, fileTable1, heap1);
//        stack1.push(ex1);
//        repo1.addProgram(prg1);
//        Controller ctrl1 = new Controller(repo1);
//
//        typecheckProgram(ex2);
//        Repository repo2 = new InMemRepository("log2.txt");
//        ExecutionStack stack2 = new StackExecutionStack();
//        SymbolTable symTable2 = new MapSymbolTable();
//        Out out2 = new ListOut();
//        FileTable fileTable2 = new MapFileTable();
//        Heap heap2=new Heap();
//        ProgramState prg2 = new ProgramState(stack2, symTable2, out2, fileTable2, heap2);
//        stack2.push(ex2);
//        repo2.addProgram(prg2);
//        Controller ctrl2 = new Controller(repo2);
//
//        typecheckProgram(ex3);
//        Repository repo3 = new InMemRepository("log3.txt");
//        ExecutionStack stack3 = new StackExecutionStack();
//        SymbolTable symTable3 = new MapSymbolTable();
//        Out out3 = new ListOut();
//        FileTable fileTable3 = new MapFileTable();
//        Heap heap3=new Heap();
//        ProgramState prg3 = new ProgramState(stack3, symTable3, out3, fileTable3, heap3);
//        stack3.push(ex3);
//        repo3.addProgram(prg3);
//        Controller ctrl3 = new Controller(repo3);
//
//        typecheckProgram(ex4);
//        Repository repo4 = new InMemRepository("log4.txt");
//        ExecutionStack stack4 = new StackExecutionStack();
//        SymbolTable symTable4 = new MapSymbolTable();
//        Out out4 = new ListOut();
//        FileTable fileTable4 = new MapFileTable();
//        Heap heap4=new Heap();
//        ProgramState prg4 = new ProgramState(stack4, symTable4, out4, fileTable4, heap4);
//        stack4.push(ex4);
//        repo4.addProgram(prg4);
//        Controller ctrl4 = new Controller(repo4);
//
//        typecheckProgram(ex5);
//        Repository repo5 = new InMemRepository("log5.txt");
//        ExecutionStack stack5 = new StackExecutionStack();
//        SymbolTable symTable5 = new MapSymbolTable();
//        Out out5 = new ListOut();
//        FileTable fileTable5 = new MapFileTable();
//        Heap heap5 = new Heap();
//        ProgramState prg5 = new ProgramState(stack5, symTable5, out5, fileTable5, heap5);
//        stack5.push(ex5);
//        repo5.addProgram(prg5);
//        Controller ctrl5 = new Controller(repo5);
//
//        typecheckProgram(ex6);
//        Repository repo6 = new InMemRepository("log6.txt");
//        ExecutionStack stack6 = new StackExecutionStack();
//        SymbolTable symTable6 = new MapSymbolTable();
//        Out out6 = new ListOut();
//        FileTable fileTable6 = new MapFileTable();
//        Heap heap6 = new Heap();
//        ProgramState prg6 = new ProgramState(stack6, symTable6, out6, fileTable6, heap6);
//        stack6.push(ex6);
//        repo6.addProgram(prg6);
//        Controller ctrl6 = new Controller(repo6);
//
//        typecheckProgram(ex7);
//        Repository repo7 = new InMemRepository("log7.txt");
//        ExecutionStack stack7 = new StackExecutionStack();
//        SymbolTable symTable7 = new MapSymbolTable();
//        Out out7 = new ListOut();
//        FileTable fileTable7 = new MapFileTable();
//        Heap heap7 = new Heap();
//        ProgramState prg7 = new ProgramState(stack7, symTable7, out7, fileTable7, heap7);
//        stack7.push(ex7);
//        repo7.addProgram(prg7);
//        Controller ctrl7 = new Controller(repo7);
//
//        typecheckProgram(ex8);
//        Repository repo8 = new InMemRepository("log8.txt");
//        ExecutionStack stack8 = new StackExecutionStack();
//        SymbolTable symTable8 = new MapSymbolTable();
//        Out out8 = new ListOut();
//        FileTable fileTable8 = new MapFileTable();
//        Heap heap8 = new Heap();
//        ProgramState prg8 = new ProgramState(stack8, symTable8, out8, fileTable8, heap8);
//        stack8.push(ex8);
//        repo8.addProgram(prg8);
//        Controller ctrl8 = new Controller(repo8);
//
//        typecheckProgram(ex9);
//        ExecutionStack stack9 = new StackExecutionStack();
//        SymbolTable symTable9 = new MapSymbolTable();
//        Out out9= new ListOut();
//        FileTable fileTable9= new MapFileTable();
//        Heap heap9 = new Heap();
//        ProgramState prg9 = new ProgramState(stack9, symTable9, out9, fileTable9, heap9);
//        stack9.push(ex9);
//        Repository repo9 = new InMemRepository("log9.txt");
//        repo9.addProgram(prg9);
//        Controller ctrl9= new Controller(repo9);
//
//        typecheckProgram(ex10);
//        ExecutionStack stack10 = new StackExecutionStack();
//        SymbolTable symTable10 = new MapSymbolTable();
//        Out out10= new ListOut();
//        FileTable fileTable10= new MapFileTable();
//        Heap heap10= new Heap();
//        ProgramState prg10 = new ProgramState(stack10, symTable10, out10, fileTable10, heap10);
//        stack10.push(ex10);
//        Repository repo10= new InMemRepository("log10.txt");
//        repo10.addProgram(prg10);
//        Controller ctrl10= new Controller(repo10);
//
//
//
//
//
//        TextMenu menu=new TextMenu();
//        menu.addCommand(new ExitCommand("0","Exit the interpreter"));
//        menu.addCommand(new RunExampleCommand("1","Example 1 (int v; v=2; print(v))",ctrl1));
//        menu.addCommand(new RunExampleCommand("2","Example 2 (int a,b; a=2+3*5; b=a+1; print(b))",ctrl2));
//        menu.addCommand(new RunExampleCommand("3","Example 3 (bool a; int v; a=true; if(a) then v=2 else v=3; print(v))",ctrl3));
//        menu.addCommand(new RunExampleCommand("4","Example 4 (file operations: open, read, print, close)",ctrl4));
//        menu.addCommand(new RunExampleCommand("5", "Example 5 (Heap: new)", ctrl5));
//        menu.addCommand(new RunExampleCommand("6", "Example 6 (Heap: readHeap rH)", ctrl6));
//        menu.addCommand(new RunExampleCommand("7", "Example 7 (Heap: writeHeap wH)", ctrl7));
//        menu.addCommand(new RunExampleCommand("8", "Example 8 (GC test: Ref int v; new(v,20); Ref Ref int a; new(a,v); new(v,30); print(rH(rH(a))))", ctrl8));
//        menu.addCommand(new RunExampleCommand("9", "Example 9 (while loop: v=4; while(v>0){print(v); v=v-1;} print(v))", ctrl9));
//        menu.addCommand(new RunExampleCommand("10", "Example 10 (fork test)", ctrl10));
//
//        menu.show();
//
//
//
//    }
//
//}