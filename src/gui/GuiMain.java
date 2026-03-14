package gui;

import controller.Controller;
import exceptions.MyException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.expression.*;
import model.state.*;
import model.statement.*;
import model.type.*;
import model.value.*;
import repository.InMemRepository;
import repository.Repository;
import gui.ProgramExecutionView;

import javax.naming.Context;
import java.util.ArrayList;
import java.util.List;

public class GuiMain extends Application {

    private final List<Statement> examples = new ArrayList<>();
    private final List<String> exampleDescriptions = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        buildExamples();

        ListView<String> programsListView = new ListView<>();
        programsListView.setItems(FXCollections.observableArrayList(exampleDescriptions));

        Button runButton = new Button("Run selected program");
        runButton.setOnAction(e -> {
            int index = programsListView.getSelectionModel().getSelectedIndex();
            if (index < 0) {
                new Alert(Alert.AlertType.WARNING, "Select a program first!").showAndWait();
                return;
            }
            Statement stmt = examples.get(index);

            try {
                // typecheck (optional but recommended)
                MyIDictionary<String, Type> typeEnv = new MyDictionary<>();
                stmt.typecheck(typeEnv);


                Controller controller = buildControllerFor(stmt, "log" + (index + 1) + "_gui.txt");


                openExecutionWindow(controller, "Example " +  (index + 1));
            } catch (MyException ex) {
                new Alert(Alert.AlertType.ERROR,
                        "Typecheck error: " + ex.getMessage()).showAndWait();
            }
        });

        VBox leftBox = new VBox(10, new Label("Select a program:"), programsListView, runButton);
        leftBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane(leftBox);
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setTitle("Toy Language – Program selector");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Controller buildControllerFor(Statement program, String logFile) {
        Repository repo = new InMemRepository(logFile);
        ExecutionStack stack = new StackExecutionStack();
        SymbolTable symTable = new MapSymbolTable();
        Out out = new ListOut();
        FileTable fileTable = new MapFileTable();
        Heap heap = new Heap();
        CountSemaphore countSemaphore=new CountSemaphore();
        BarrierTable barrierTable= new BarrierTable();
        LockTable lockTable=new  LockTable();
        CountDownLatch countDownLatch=new CountDownTable();

        ProgramState prg = new ProgramState(stack, symTable, out, fileTable, heap, countSemaphore,barrierTable,lockTable, countDownLatch);
        stack.push(program);
        repo.addProgram(prg);

        return new Controller(repo);
    }

    private void openExecutionWindow(Controller controller, String title) {
        Stage stage = new Stage();
        ProgramExecutionView view = new ProgramExecutionView(controller);
        Scene scene = new Scene(view.getRoot(), 900, 600);
        stage.setTitle("Toy Language – " + title);
        stage.setScene(scene);
        stage.show();
    }

    // --- build the same 10 examples you already have in Interpreter.main() ---
    private void buildExamples() {
        Statement ex1 = new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "v"),
                new CompoundStatement(
                        new AssignmentStatement(new ConstantExpression(new IntegerValue(2)), "v"),
                        new PrintStatement(new VariableExpression("v"))));

        Statement ex2 = new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "a"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new IntType(), "b"),
                        new CompoundStatement(
                                new AssignmentStatement(
                                        new BinaryOperatorExpression("+",
                                                new ConstantExpression(new IntegerValue(2)),
                                                new BinaryOperatorExpression("*",
                                                        new ConstantExpression(new IntegerValue(3)),
                                                        new ConstantExpression(new IntegerValue(5)))),
                                        "a"),
                                new CompoundStatement(
                                        new AssignmentStatement(
                                                new BinaryOperatorExpression("+",
                                                        new VariableExpression("a"),
                                                        new ConstantExpression(new IntegerValue(1))),
                                                "b"),
                                        new PrintStatement(new VariableExpression("b"))))));

        Statement ex3 = new CompoundStatement(
                new VariableDeclarationStatement(new BoolType(), "a"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new IntType(), "v"),
                        new CompoundStatement(
                                new AssignmentStatement(new ConstantExpression(new BooleanValue(true)), "a"),
                                new CompoundStatement(
                                        new IfStatement(new VariableExpression("a"),
                                                new AssignmentStatement(new ConstantExpression(new IntegerValue(2)), "v"),
                                                new AssignmentStatement(new ConstantExpression(new IntegerValue(3)), "v")),
                                        new PrintStatement(new VariableExpression("v"))))));

        Statement ex4 =
                new CompoundStatement(
                        new VariableDeclarationStatement(new StringType(), "varf"),
                        new CompoundStatement(
                                new AssignmentStatement(
                                        new ConstantExpression(new StringValue("test.in")), "varf"
                                ),
                                new CompoundStatement(
                                        new OpenRFileStatement(new VariableExpression("varf")),
                                        new CompoundStatement(
                                                new VariableDeclarationStatement(new IntType(), "varc"),
                                                new CompoundStatement(
                                                        new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                        new CompoundStatement(
                                                                new PrintStatement(new VariableExpression("varc")),
                                                                new CompoundStatement(
                                                                        new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new VariableExpression("varc")),
                                                                                new CloseRFileStatement(new VariableExpression("varf"))
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );

        Statement ex5 = new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntType()), "v"),
                new CompoundStatement(
                        new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                        new CompoundStatement(
                                new VariableDeclarationStatement(
                                        new RefType(new RefType(new IntType())), "a"),
                                new CompoundStatement(
                                        new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(
                                                new PrintStatement(new VariableExpression("v")),
                                                new PrintStatement(new VariableExpression("a")))))));

        Statement ex6 = new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntType()), "v"),
                new CompoundStatement(
                        new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                        new CompoundStatement(
                                new VariableDeclarationStatement(new RefType(new RefType(new IntType())), "a"),
                                new CompoundStatement(
                                        new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(
                                                new PrintStatement(
                                                        new ReadHeapExpression(new VariableExpression("v"))),
                                                new PrintStatement(
                                                        new BinaryOperatorExpression("+",
                                                                new ReadHeapExpression(
                                                                        new ReadHeapExpression(new VariableExpression("a"))),
                                                                new ConstantExpression(new IntegerValue(5)))))))));

        Statement ex7 = new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntType()), "v"),
                new CompoundStatement(
                        new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                        new CompoundStatement(
                                new PrintStatement(
                                        new ReadHeapExpression(new VariableExpression("v"))),
                                new CompoundStatement(
                                        new WriteHeapStatement("v",
                                                new ConstantExpression(new IntegerValue(30))),
                                        new PrintStatement(
                                                new BinaryOperatorExpression("+",
                                                        new ReadHeapExpression(new VariableExpression("v")),
                                                        new ConstantExpression(new IntegerValue(5))))))));

        Statement ex8 = new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntType()), "v"),
                new CompoundStatement(
                        new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                        new CompoundStatement(
                                new VariableDeclarationStatement(new RefType(new RefType(new IntType())), "a"),
                                new CompoundStatement(
                                        new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(
                                                new NewStatement("v", new ConstantExpression(new IntegerValue(30))),
                                                new PrintStatement(
                                                        new ReadHeapExpression(
                                                                new ReadHeapExpression(
                                                                        new VariableExpression("a")))))))));

        Statement ex9 = new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "v"),
                new CompoundStatement(
                        new AssignmentStatement(new ConstantExpression(new IntegerValue(4)), "v"),
                        new CompoundStatement(
                                new WhileStatement(
                                        new RelationExpression(">",
                                                new VariableExpression("v"),
                                                new ConstantExpression(new IntegerValue(0))),
                                        new CompoundStatement(
                                                new PrintStatement(new VariableExpression("v")),
                                                new AssignmentStatement(
                                                        new BinaryOperatorExpression("-",
                                                                new VariableExpression("v"),
                                                                new ConstantExpression(new IntegerValue(1))),
                                                        "v"))),
                                new PrintStatement(new VariableExpression("v")))));

        Statement ex10 = new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "v"),
                new CompoundStatement(
                        new AssignmentStatement(new ConstantExpression(new IntegerValue(10)), "v"),
                        new CompoundStatement(
                                new VariableDeclarationStatement(new RefType(new IntType()), "a"),
                                new CompoundStatement(
                                        new NewStatement("a", new ConstantExpression(new IntegerValue(22))),
                                        new CompoundStatement(
                                                new ForkStatement(
                                                        new CompoundStatement(
                                                                new WriteHeapStatement("a",
                                                                        new ConstantExpression(new IntegerValue(30))),
                                                                new CompoundStatement(
                                                                        new AssignmentStatement(
                                                                                new ConstantExpression(new IntegerValue(32)), "v"),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new VariableExpression("v")),
                                                                                new PrintStatement(
                                                                                        new ReadHeapExpression(
                                                                                                new VariableExpression("a"))))))),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(
                                                                new ReadHeapExpression(
                                                                        new VariableExpression("a")))))))));

        examples.add(ex1);  exampleDescriptions.add("Example 1: int v; v=2; print(v)");
        examples.add(ex2);  exampleDescriptions.add("Example 2: int a,b; a=2+3*5; b=a+1; print(b)");
        examples.add(ex3);  exampleDescriptions.add("Example 3: bool a; int v; a=true; if(a) then v=2 else v=3; print(v)");
        examples.add(ex4);  exampleDescriptions.add("Example 4: file operations");
        examples.add(ex5);  exampleDescriptions.add("Example 5: heap – new");
        examples.add(ex6);  exampleDescriptions.add("Example 6: heap – readHeap");
        examples.add(ex7);  exampleDescriptions.add("Example 7: heap – writeHeap");
        examples.add(ex8);  exampleDescriptions.add("Example 8: GC test");
        examples.add(ex9);  exampleDescriptions.add("Example 9: while loop");
        examples.add(ex10); exampleDescriptions.add("Example 10: fork");

        Statement forstmt=new CompoundStatement(new VariableDeclarationStatement(new IntType(),"v"),
            new CompoundStatement(new VariableDeclarationStatement(new RefType(new IntType()), "a"),
                new CompoundStatement(new NewStatement("a", new ConstantExpression(new IntegerValue(20))),
                        new CompoundStatement(
                                new ForStmt(
                                        "v", new ConstantExpression(new IntegerValue(0)), new ConstantExpression(new IntegerValue(3)),
                                        new BinaryOperatorExpression("+",new VariableExpression("v"), new ConstantExpression(new IntegerValue(1))),
                                new ForkStatement(new CompoundStatement(new PrintStatement(new VariableExpression("v")), new AssignmentStatement(new BinaryOperatorExpression("*",new VariableExpression("v"), new ReadHeapExpression(new VariableExpression("a"))),"v")))
                        ),new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                        )
                )
            )
        );



        examples.add(forstmt); exampleDescriptions.add("For");


        Statement switchex=new CompoundStatement(new VariableDeclarationStatement(new IntType(), "a"),
                new CompoundStatement(new VariableDeclarationStatement(new IntType(),"b"),
                        new CompoundStatement(new VariableDeclarationStatement(new IntType(), "c"),
                                new CompoundStatement(new AssignmentStatement(new ConstantExpression(new IntegerValue(1)),"a"),
                                        new CompoundStatement(new AssignmentStatement(new ConstantExpression(new IntegerValue(2)),"b"),
                                                new CompoundStatement(new AssignmentStatement(new ConstantExpression(new IntegerValue(5)),"c"),
                                                        new CompoundStatement(
                                                                new SwitchStmt(
                                                                        new BinaryOperatorExpression("*",new VariableExpression("a"), new ConstantExpression(new IntegerValue(10))),
                                                                        new BinaryOperatorExpression("*", new VariableExpression("b"), new VariableExpression("c")),
                                                                        new CompoundStatement(new PrintStatement(new VariableExpression("a")), new PrintStatement(new VariableExpression("b"))),
                                                                        new ConstantExpression(new IntegerValue(10)),
                                                                        new CompoundStatement(new PrintStatement(new ConstantExpression(new IntegerValue(100))), new PrintStatement(new ConstantExpression(new IntegerValue(200)))),
                                                                        new PrintStatement(new ConstantExpression(new IntegerValue(300)))

                                                                ),
                                                                new PrintStatement(new ConstantExpression(new IntegerValue(300))))
                                                        )
                                        )
                                )
                                )));

        examples.add(switchex); exampleDescriptions.add("Switch");




        Statement exdowhile=new CompoundStatement(
                new VariableDeclarationStatement(new IntType(),"v"),
                new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "x"),
                        new CompoundStatement(new VariableDeclarationStatement(new IntType(),"y"),
                                new CompoundStatement(
                                        new AssignmentStatement(new ConstantExpression(new IntegerValue(0)),"v"),
                                        new CompoundStatement(
                                                    new RepeatUntil(new CompoundStatement(new ForkStatement(new CompoundStatement(
                                                                                                            new PrintStatement(new VariableExpression("v")),
                                                                                                            new AssignmentStatement(new BinaryOperatorExpression("-",new VariableExpression("v"), new ConstantExpression(new IntegerValue(1))),"v")
                                                    )),
                                                                                         new AssignmentStatement(new BinaryOperatorExpression("+",new VariableExpression("v"),new ConstantExpression(new IntegerValue(1))),"v")),
                                                                     new RelationExpression("==",new VariableExpression("v"),new ConstantExpression(new IntegerValue(3)))
                                                    ),
                                                new CompoundStatement(new AssignmentStatement(new ConstantExpression(new IntegerValue(1)),"x"),
                                                        new CompoundStatement(new NoOperationStatement(),
                                                                new CompoundStatement(new AssignmentStatement(new ConstantExpression(new IntegerValue(3)),"y"),
                                                                        new CompoundStatement(new NoOperationStatement(),new PrintStatement(new BinaryOperatorExpression("*",new VariableExpression("v"), new ConstantExpression(new IntegerValue(10)))))
                                                                )
                                                        )
                                                 ))
                                )
                        )
                        )
        );
        examples.add(exdowhile); exampleDescriptions.add("DoWhile");



        Statement CondAssig=new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntType()),"a"),
                new CompoundStatement(new VariableDeclarationStatement(new RefType(new IntType()),"b"),
                        new CompoundStatement(new VariableDeclarationStatement(new IntType(),"v"),
                                new CompoundStatement(new NewStatement("a", new ConstantExpression(new IntegerValue(0))),
                                        new CompoundStatement(new NewStatement("b",new ConstantExpression(new IntegerValue(0))),
                                                new CompoundStatement(new WriteHeapStatement("a", new ConstantExpression(new IntegerValue(1))),
                                                        new CompoundStatement(
                                                                new WriteHeapStatement("b",new ConstantExpression(new IntegerValue(2))),
                                                                new CompoundStatement(
                                                                        new CondAssigStatement("v",
                                                                                new RelationExpression("<",
                                                                                        new ReadHeapExpression(new VariableExpression("a")),
                                                                                        new ReadHeapExpression(new VariableExpression("b"))
                                                                                ), new ConstantExpression(new IntegerValue(100)),
                                                                                new ConstantExpression(new IntegerValue(200))
                                                                        ),
                                                                        new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                                                new CompoundStatement
                                                                                        (new CondAssigStatement("v",
                                                                                                new RelationExpression(">",
                                                                                                        new BinaryOperatorExpression("-",
                                                                                                                new ReadHeapExpression(new VariableExpression("b") ),
                                                                                                                new ConstantExpression(new IntegerValue(2))),
                                                                                                        new ReadHeapExpression(new VariableExpression("a"))),
                                                                                                new ConstantExpression(new IntegerValue(100)),
                                                                                                new ConstantExpression(new IntegerValue(200))
                                                                                        ), new PrintStatement(new VariableExpression("v"))
                                                                                        )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        examples.add(CondAssig); exampleDescriptions.add("CondAssig");


        Statement countSempahore=new CompoundStatement(
                new VariableDeclarationStatement( new RefType(new IntType()), "v1"),
                new CompoundStatement( new VariableDeclarationStatement(new IntType(),"cnt"),
                        new CompoundStatement(new NewStatement("v1", new ConstantExpression(new IntegerValue(1))),
                                new CompoundStatement(new CreateSemaphore("cnt", new ReadHeapExpression(new VariableExpression("v1"))),
                                        new CompoundStatement(
                                                new ForkStatement(new CompoundStatement(new AcquireStatement("cnt"),
                                                        new CompoundStatement(new WriteHeapStatement("v1", new BinaryOperatorExpression("*",new ReadHeapExpression(new VariableExpression("v1")),new ConstantExpression(new IntegerValue(10)))),
                                                                new CompoundStatement(
                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v1") )),
                                                                        new RelaseStatement("cnt"))
                                                        )
                                                )

                                                ),
                                                new CompoundStatement(
                                                        new ForkStatement(new CompoundStatement(new AcquireStatement("cnt"),
                                                                new CompoundStatement(new WriteHeapStatement("v1", new BinaryOperatorExpression("*",new ReadHeapExpression(new VariableExpression("v1")),new ConstantExpression(new IntegerValue(10)))),
                                                                        new CompoundStatement(new WriteHeapStatement("v1",new BinaryOperatorExpression("*",new ReadHeapExpression(new VariableExpression("v1")),new ConstantExpression(new IntegerValue(2)))),
                                                                                new CompoundStatement( new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                        new RelaseStatement("cnt")))
                                                                )
                                                        )
                                                        ),
                                                        new CompoundStatement(new AcquireStatement("cnt"),
                                                                new CompoundStatement(new PrintStatement(new BinaryOperatorExpression("-", new ReadHeapExpression(new VariableExpression("v1")), new ConstantExpression(new IntegerValue(1)))),
                                                                        new RelaseStatement("cnt"))
                                                        )

                                                )
                                        )
                                )
                        )
                )
        );
        examples.add(countSempahore); exampleDescriptions.add("CountSemaphore");



        Statement exBarrier=new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntType()),"v1"),
                new CompoundStatement(new VariableDeclarationStatement(new RefType(new IntType()), "v2"),
                        new CompoundStatement(new VariableDeclarationStatement(new RefType(new IntType()),"v3"),
                                new CompoundStatement(new VariableDeclarationStatement(new IntType(),"cnt"),
                                        new CompoundStatement(new NewStatement("v1", new ConstantExpression(new IntegerValue(2))),
                                                new CompoundStatement(new NewStatement("v2", new ConstantExpression(new IntegerValue(3))),
                                                        new CompoundStatement(new NewStatement("v3", new ConstantExpression(new IntegerValue(4))),
                                                                new CompoundStatement(new NewBarrier("cnt", new ReadHeapExpression(new VariableExpression("v2"))),
                                                                        new CompoundStatement(new ForkStatement( new CompoundStatement(new AwaitStatement("cnt"),
                                                                                new CompoundStatement(
                                                                                        new WriteHeapStatement("v1",new BinaryOperatorExpression("*",new ReadHeapExpression(new VariableExpression("v1")),
                                                                                                new ConstantExpression(new IntegerValue(10)))),
                                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v1")
                                                                                        )
                                                                                        )
                                                                                ))
                                                                        ),

                                                                                new CompoundStatement(new ForkStatement( new CompoundStatement(new AwaitStatement("cnt"),
                                                                                        new CompoundStatement(new WriteHeapStatement("v2", new BinaryOperatorExpression("*",
                                                                                                new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                new ConstantExpression(new IntegerValue(10))
                                                                                        )
                                                                                        ), new CompoundStatement(new WriteHeapStatement("v2", new BinaryOperatorExpression("*",
                                                                                                new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                new ConstantExpression(new IntegerValue(10)))),
                                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v2"))))
                                                                                        ))) ,
                                                                                        new CompoundStatement(new AwaitStatement("cnt"),new PrintStatement(new ReadHeapExpression(new VariableExpression("v3")))
                                                                                        )
                                                                                )

                                                                        )
                                                                )
                                                        )

                                                )
                                        )
                                )
                        )
                )
        );

        examples.add(exBarrier); exampleDescriptions.add("Barrier");



        Statement exlock=new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntType()),"v1"),
                new CompoundStatement(new VariableDeclarationStatement(new RefType(new IntType()),"v2"),
                        new CompoundStatement(new VariableDeclarationStatement(new IntType(),"x"),
                                new CompoundStatement(new VariableDeclarationStatement(new IntType(),"q"),
                                        new CompoundStatement(new NewStatement("v1", new ConstantExpression(new IntegerValue(20))),
                                                new CompoundStatement(new NewStatement("v2", new ConstantExpression(new IntegerValue(30))),
                                                        new CompoundStatement(new newLock("x"),
                                                                new CompoundStatement(new ForkStatement(
                                                                        new CompoundStatement(new ForkStatement
                                                                                (new CompoundStatement(new LockStmt("x"),
                                                                                        new CompoundStatement(new WriteHeapStatement("v1", new BinaryOperatorExpression("-",new ReadHeapExpression(new VariableExpression("v1")), new ConstantExpression(new IntegerValue(1)))),
                                                                                                new UnlockStmt("x"))

                                                                                )
                                                                                ), new CompoundStatement(new LockStmt("x"),
                                                                                new CompoundStatement(new WriteHeapStatement("v1", new BinaryOperatorExpression("*", new ReadHeapExpression(new VariableExpression("v1")), new ConstantExpression(new IntegerValue(10)))),
                                                                                        new UnlockStmt("x"))
                                                                        )
                                                                        )

                                                                ), new CompoundStatement( new newLock("q"),
                                                                        new CompoundStatement(new ForkStatement(
                                                                                new CompoundStatement(new ForkStatement(
                                                                                        new CompoundStatement(
                                                                                                new LockStmt("q"),
                                                                                                new CompoundStatement( new WriteHeapStatement("v2",
                                                                                                        new BinaryOperatorExpression("+", new ReadHeapExpression(new VariableExpression("v2")),new ConstantExpression(new IntegerValue(5)))),
                                                                                                        new UnlockStmt("q"))
                                                                                        )
                                                                                ),new CompoundStatement(new LockStmt("q"),
                                                                                        new CompoundStatement(new WriteHeapStatement("v2", new BinaryOperatorExpression("*", new ReadHeapExpression(new VariableExpression("v2")), new ConstantExpression(new IntegerValue(10)))),
                                                                                                new UnlockStmt("q")))
                                                                                )
                                                                        ),new CompoundStatement(new NoOperationStatement(),
                                                                                new CompoundStatement(new NoOperationStatement(),
                                                                                        new CompoundStatement(new NoOperationStatement(),
                                                                                                new CompoundStatement(new NoOperationStatement(),
                                                                                                        new CompoundStatement(  new LockStmt("x"),
                                                                                                                new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                                                        new CompoundStatement(new UnlockStmt("x"),
                                                                                                                                new CompoundStatement(new LockStmt("q"),
                                                                                                                                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v2"))),
                                                                                                                                                new UnlockStmt("q"))
                                                                                                                                )
                                                                                                                        )
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                        )
                                                                )


                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )

        );
        examples.add(exlock); exampleDescriptions.add("Lock");



        Statement stmt =
                new CompoundStatement(
                        new VariableDeclarationStatement(new RefType(new IntType()), "v1"),
                        new CompoundStatement(
                                new VariableDeclarationStatement(new RefType(new IntType()), "v2"),
                                new CompoundStatement(
                                        new VariableDeclarationStatement(new RefType(new IntType()), "v3"),
                                        new CompoundStatement(
                                                new VariableDeclarationStatement(new IntType(), "cnt"),
                                                new CompoundStatement(
                                                        new NewStatement("v1", new ConstantExpression(new IntegerValue(2))),
                                                        new CompoundStatement(
                                                                new NewStatement("v2", new ConstantExpression(new IntegerValue(3))),
                                                                new CompoundStatement(
                                                                        new NewStatement("v3", new ConstantExpression(new IntegerValue(4))),
                                                                        new CompoundStatement(
                                                                                new newLatch(
                                                                                        "cnt",
                                                                                        new ReadHeapExpression(
                                                                                                new VariableExpression("v2")
                                                                                        )
                                                                                ),
                                                                                new CompoundStatement(
                                                                                        new ForkStatement(
                                                                                                new CompoundStatement(
                                                                                                        new WriteHeapStatement(
                                                                                                                "v1",
                                                                                                                new BinaryOperatorExpression(
                                                                                                                        "*",
                                                                                                                        new ReadHeapExpression(new VariableExpression("v1")),
                                                                                                                        new ConstantExpression(new IntegerValue(10))
                                                                                                                )
                                                                                                        ),
                                                                                                        new CompoundStatement(
                                                                                                                new PrintStatement(
                                                                                                                        new ReadHeapExpression(new VariableExpression("v1"))
                                                                                                                ),
                                                                                                                new CompoundStatement(
                                                                                                                        new CountDownStmt("cnt"),
                                                                                                                        new ForkStatement(
                                                                                                                                new CompoundStatement(
                                                                                                                                        new WriteHeapStatement(
                                                                                                                                                "v2",
                                                                                                                                                new BinaryOperatorExpression(
                                                                                                                                                        "*",
                                                                                                                                                        new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                                                        new ConstantExpression(new IntegerValue(10))
                                                                                                                                                )
                                                                                                                                        ),
                                                                                                                                        new CompoundStatement(
                                                                                                                                                new PrintStatement(
                                                                                                                                                        new ReadHeapExpression(new VariableExpression("v2"))
                                                                                                                                                ),
                                                                                                                                                new CompoundStatement(
                                                                                                                                                        new CountDownStmt("cnt"),
                                                                                                                                                        new ForkStatement(
                                                                                                                                                                new CompoundStatement(
                                                                                                                                                                        new WriteHeapStatement(
                                                                                                                                                                                "v3",
                                                                                                                                                                                new BinaryOperatorExpression(
                                                                                                                                                                                        "*",
                                                                                                                                                                                        new ReadHeapExpression(new VariableExpression("v3")),
                                                                                                                                                                                        new ConstantExpression(new IntegerValue(10))
                                                                                                                                                                                )
                                                                                                                                                                        ),
                                                                                                                                                                        new CompoundStatement(
                                                                                                                                                                                new PrintStatement(
                                                                                                                                                                                        new ReadHeapExpression(new VariableExpression("v3"))
                                                                                                                                                                                ),
                                                                                                                                                                                new CountDownStmt("cnt")
                                                                                                                                                                        )
                                                                                                                                                                )
                                                                                                                                                        )
                                                                                                                                                )
                                                                                                                                        )
                                                                                                                                )
                                                                                                                        )
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        ),
                                                                                        new CompoundStatement(
                                                                                                new AwaitStmt("cnt"),
                                                                                                new CompoundStatement(
                                                                                                        new PrintStatement(
                                                                                                                new ConstantExpression(new IntegerValue(100))
                                                                                                        ),
                                                                                                        new CompoundStatement(
                                                                                                                new CountDownStmt("cnt"),
                                                                                                                new PrintStatement(
                                                                                                                        new ConstantExpression(new IntegerValue(100))
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );



        examples.add( stmt); exampleDescriptions.add("CountDown");


    }
}

