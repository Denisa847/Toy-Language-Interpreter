package gui;

import controller.Controller;
import exceptions.MyException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import model.state.CountSemaphore;
import model.state.ProgramState;
import model.statement.Statement;
import model.value.Value;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProgramExecutionView {

    private final Controller controller;


    // UI controls
    private final BorderPane root = new BorderPane();

    private final TextField nrPrgStatesField = new TextField();
    private final ListView<Integer> prgStatesListView = new ListView<>();
    private final ListView<String> exeStackListView = new ListView<>();
    private final ListView<String> outListView = new ListView<>();
    private final ListView<String> fileTableListView = new ListView<>();

    private final TableView<HeapEntry> heapTableView = new TableView<>();
    private final TableView<SymTableEntry> symTableView = new TableView<>();
    private final TableView<CntSemaphoreEntry> csemTableView=new TableView<>();
    private final TableView<BarrierEntry> barrierTableView=new TableView<>();
    private final TableView<LockTableEntry> lockTableView=new TableView<>();
    private final TableView<CountDownEntry> cntTableView=new TableView<>();

    private final Button oneStepButton = new Button("Run one step");

    public ProgramExecutionView(Controller controller) {
        this.controller = controller;
        buildUI();
        populateAll();
    }

    public Parent getRoot() {
        return root;
    }



    private void buildUI() {
        // left side: program IDs + button + #states
        nrPrgStatesField.setEditable(false);

        VBox leftBox = new VBox(10);
        leftBox.setPadding(new Insets(10));
        leftBox.getChildren().addAll(
                new Label("Program IDs"),
                prgStatesListView,
                new Label("Number of ProgramStates"),
                nrPrgStatesField,
                oneStepButton
        );


        configureHeapTable();
        configureSymTable();
        configureCntSemaphore();
        configureBarTable();
        configureLockTable();
        configureCountDown();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);


        grid.add(new VBox(new Label("Heap"), heapTableView), 0, 0);
        grid.add(new VBox(new Label("Symbol Table"), symTableView), 1, 0);
        grid.add(new VBox(new Label("CountSemaphore"),  csemTableView), 2,0);
        grid.add(new VBox(new Label("Barrier table"), barrierTableView), 3, 0);
        grid.add(new VBox(new Label("Lock Table"), lockTableView), 4, 0);
        grid.add(new VBox(new Label("CountDownLatch"),  cntTableView), 5,0);


        grid.add(new VBox(new Label("Execution Stack"), exeStackListView), 0, 1);

        VBox outFileBox = new VBox(5,
                new Label("Output"),
                outListView,
                new Label("File Table"),
                fileTableListView);
        grid.add(outFileBox, 1, 1);

        root.setLeft(leftBox);
        root.setCenter(grid);

        // events
        prgStatesListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldV, newV) -> populateForSelectedProgram());

        oneStepButton.setOnAction(e -> runOneStep());
    }

    private void  configureCountDown(){
        TableColumn<CountDownEntry, Integer> locCol=new TableColumn<>("Location");
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<CountDownEntry, Integer> valueCol=new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));


        cntTableView.getColumns().addAll(locCol,valueCol);
        cntTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void  configureLockTable(){
        TableColumn<LockTableEntry, Integer> locCol = new TableColumn<>("Location");
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<LockTableEntry, Integer> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        lockTableView.getColumns().addAll(locCol, valueCol);
        lockTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    private void configureBarTable(){
        TableColumn<BarrierEntry,Integer> indexCol=new TableColumn<>("Index");
        indexCol.setCellValueFactory(new PropertyValueFactory<>("index"));

        TableColumn<BarrierEntry,Integer> valueCol=new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        TableColumn<BarrierEntry, String> listCol = new TableColumn<>("Waiting List");
        listCol.setCellValueFactory(new PropertyValueFactory<>("list"));

        barrierTableView.getColumns().addAll(indexCol,valueCol,listCol);
        barrierTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void  configureCntSemaphore(){
        TableColumn<CntSemaphoreEntry, Integer> idCol=new TableColumn<>("Index");
        idCol.setCellValueFactory(new PropertyValueFactory<>("index"));

        TableColumn<CntSemaphoreEntry, String> valueCol=new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        TableColumn<CntSemaphoreEntry, String> listCol=new TableColumn<>("List");
        listCol.setCellValueFactory(new PropertyValueFactory<>("list"));

        csemTableView.getColumns().addAll(idCol,valueCol,listCol);
        csemTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configureHeapTable() {
        TableColumn<HeapEntry, Integer> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<HeapEntry, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        heapTableView.getColumns().addAll(addressCol, valueCol);
        heapTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configureSymTable() {
        TableColumn<SymTableEntry, String> varCol = new TableColumn<>("Variable");
        varCol.setCellValueFactory(new PropertyValueFactory<>("varName"));

        TableColumn<SymTableEntry, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        symTableView.getColumns().addAll(varCol, valueCol);
        symTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


    }



    private void populateAll() {
        List<ProgramState> states = controller.getProgramList();
        nrPrgStatesField.setText(String.valueOf(states.size()));

        List<Integer> ids = states.stream()
                .map(ProgramState::getId)
                .collect(Collectors.toList());
        prgStatesListView.setItems(FXCollections.observableArrayList(ids));

        if (!ids.isEmpty() && prgStatesListView.getSelectionModel().getSelectedItem() == null) {
            prgStatesListView.getSelectionModel().selectFirst();
        }

        populateHeap();
        populateForSelectedProgram();
        populateCountSemaphore();
        populateBarrier();
        populateLock();
        populateCountDownLatch();
    }

    private void populateCountDownLatch(){
        List<ProgramState> states = controller.getProgramList();
        if (states.isEmpty()) {
            cntTableView.getItems().clear();
            return;
        }

        Map<Integer, Integer> countContent = states.get(0).countDownLatch().getContent();
        List<CountDownEntry> entries =countContent.entrySet().stream()
                .map(e -> new CountDownEntry(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        cntTableView.setItems(FXCollections.observableArrayList(entries));
    }

    private void populateLock(){
        List<ProgramState> states = controller.getProgramList();
        if (states.isEmpty()) {
            lockTableView.getItems().clear();
            return;
        }
        Map<Integer,Integer> lockContent=states.get(0).lockTable().getContent();
        List<LockTableEntry> entries=lockContent.entrySet().stream()
                .map(e -> new LockTableEntry(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        lockTableView.setItems(FXCollections.observableArrayList(entries));
    }




    private void populateBarrier(){
        List<ProgramState> states = controller.getProgramList();
        if (states.isEmpty()) {
            barrierTableView.getItems().clear();
            return;
        }

        Map<Integer, Pair<Integer, List<Integer>>> barrierContent = states.get(0).barrierTable().getContent();
        List<BarrierEntry> entries = barrierContent .entrySet().stream()
                .map(e -> new BarrierEntry(e.getKey(), e.getValue().getKey(),e.getValue().getValue().toString()))
                .collect(Collectors.toList());
        barrierTableView.setItems(FXCollections.observableArrayList(entries));
    }

    private void populateCountSemaphore(){
        List<ProgramState> states = controller.getProgramList();
        if (states.isEmpty()) {
            csemTableView.getItems().clear();
            return;
        }

        Map<Integer, Pair<Integer,List<Integer>>> countSemaphoreContent = states.get(0).countSemaphore().getContent();
        List<CntSemaphoreEntry> entries =countSemaphoreContent.entrySet().stream()
                .map(e -> new CntSemaphoreEntry(e.getKey(), e.getValue().getKey().toString(), e.getValue().getValue().toString()))
                .collect(Collectors.toList());
        csemTableView.setItems(FXCollections.observableArrayList(entries));
    }


    private void populateHeap() {
        List<ProgramState> states = controller.getProgramList();
        if (states.isEmpty()) {
            heapTableView.getItems().clear();
            return;
        }

        Map<Integer, Value> heapContent = states.get(0).heap().getContent();
        List<HeapEntry> entries = heapContent.entrySet().stream()
                .map(e -> new HeapEntry(e.getKey(), e.getValue().toString()))
                .collect(Collectors.toList());
        heapTableView.setItems(FXCollections.observableArrayList(entries));
    }

    private ProgramState getSelectedProgramState() {
        Integer selectedId = prgStatesListView.getSelectionModel().getSelectedItem();
        if (selectedId == null) return null;

        for (ProgramState st : controller.getProgramList()) {
            if (st.getId() == selectedId) return st;
        }
        return null;
    }

    private void populateForSelectedProgram() {
        ProgramState state = getSelectedProgramState();
        if (state == null) {
            symTableView.getItems().clear();
            exeStackListView.getItems().clear();
            outListView.getItems().clear();
            fileTableListView.getItems().clear();
            return;
        }


        var symEntries = state.symbolTable().getContent().entrySet().stream()
                .map(e -> new SymTableEntry(e.getKey(), e.getValue().toString()))
                .collect(Collectors.toList());
        symTableView.setItems(FXCollections.observableArrayList(symEntries));


        List<String> stackStrings = state.executionStack().getStack().stream()
                .map(Statement::toString)
                .collect(Collectors.toList());
        exeStackListView.setItems(FXCollections.observableArrayList(stackStrings));

        // out
        List<String> outStrings = state.out().getList().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        outListView.setItems(FXCollections.observableArrayList(outStrings));

        // file table
        List<String> fileStrings = state.fileTable().getContent().entrySet().stream()
                .map(e -> e.getKey().toString())
                .collect(Collectors.toList());
        fileTableListView.setItems(FXCollections.observableArrayList(fileStrings));
    }


    private void runOneStep() {
        try {

            if (controller.getProgramList().isEmpty()) {
                // Program already done → keep final state on screen
                oneStepButton.setDisable(true);
                return;
            }

            controller.oneStepGUI();

            // After executing a step
            if (controller.getProgramList().isEmpty()) {
                // FINAL STATE → just disable button, DO NOT refresh (DO NOT clear UI)
                oneStepButton.setDisable(true);
            } else {
                // Still running → safe to refresh UI
                populateAll();
            }

        } catch (MyException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Execution error: " + e.getMessage()).showAndWait();
            oneStepButton.setDisable(true);
        }
    }

    public static class  CountDownEntry{
        private final SimpleIntegerProperty location;
        private final SimpleIntegerProperty value;

        public CountDownEntry(int location,int value){
            this.location=new SimpleIntegerProperty(location);
            this.value=new SimpleIntegerProperty(value);

        }

        public int getLocation(){return location.get();}
        public int getValue(){ return value.get();}

    }

    public static class LockTableEntry{
        private final SimpleIntegerProperty location;
        private final SimpleIntegerProperty value;

        public LockTableEntry(int location,int value){
            this.location = new SimpleIntegerProperty(location);
            this.value = new SimpleIntegerProperty(value);
        }
        public int getLocation() { return location.get(); }
        public int getValue() { return value.get(); }

    }



    public static class CntSemaphoreEntry{
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty value;
        private final SimpleStringProperty list;

        public CntSemaphoreEntry(int id, String value, String list){
            this.id=new SimpleIntegerProperty(id);
            this.value=new SimpleStringProperty(value);
            this.list=new SimpleStringProperty(list);
        }

        public int getIndex(){return id.get();}
        public String getValue(){ return value.get();}
        public String getList(){return list.get();}
    }

    public static class HeapEntry {
        private final SimpleIntegerProperty address;
        private final SimpleStringProperty value;

        public HeapEntry(int address, String value) {
            this.address = new SimpleIntegerProperty(address);
            this.value = new SimpleStringProperty(value);
        }

        public int getAddress() { return address.get(); }
        public String getValue() { return value.get(); }
    }


    public static class BarrierEntry{
        private final SimpleIntegerProperty index;
        private final SimpleIntegerProperty value;
        private final SimpleStringProperty list;
        public BarrierEntry(int index, int value, String list){
            this.index=new SimpleIntegerProperty(index);
            this.value=new SimpleIntegerProperty(value);
            this.list=new SimpleStringProperty(list);
        }

        public int getIndex(){return index.get();}
        public int getValue(){return value.get();}
        public String getList(){return list.get();}


    }


    public static class SymTableEntry {
        private final SimpleStringProperty varName;
        private final SimpleStringProperty value;

        public SymTableEntry(String varName, String value) {
            this.varName = new SimpleStringProperty(varName);
            this.value = new SimpleStringProperty(value);
        }

        public String getVarName() { return varName.get(); }
        public String getValue() { return value.get(); }
    }
}

