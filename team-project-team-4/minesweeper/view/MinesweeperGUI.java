package minesweeper.view;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import backtracker.Backtracker;
import backtracker.Configuration;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import minesweeper.model.GameState;
import minesweeper.model.Location;
import minesweeper.model.Minesweeper;
import minesweeper.model.MinesweeperException;
import minesweeper.model.MinesweeperSolver;

public class MinesweeperGUI extends Application{
    static final Image COVERED = new Image("media/images/unopened.png");
    static final Image FLAG = new Image("media/images/flag24.png");
    static final Image MINE = new Image("media/images/bomb.png");
    static final Image ZERO = new Image("media/images/0.png");
    static final Image ONE = new Image("media/images/1.png");
    static final Image TWO = new Image("media/images/2.png");
    static final Image THREE = new Image("media/images/3.png");
    static final Image FOUR = new Image("media/images/4.png");
    static final Image FIVE = new Image("media/images/5.png");
    static final Image SIX = new Image("media/images/6.png");
    static final Image SEVEN = new Image("media/images/7.png");
    static final Image EIGHT = new Image("media/images/8.png");
    static final Image HINT = new Image("media/images/hint.jpeg");

    private int ROW;
    private int COL;
    private int MINECOUNT;
    private int int_move_count = 0;

    private static GridPane GRID;
    private Label move_count = new Label(""+int_move_count);
    private Label text_at_bottom = new Label("");
    private String wonString = "You Win!";
    private String loseString = "You Lose...";
    private Minesweeper minesweeper;

    @Override
    public void start(Stage stage) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter Row: ");
        ROW = scanner.nextInt();
        System.out.println("Please enter Col: ");
        COL = scanner.nextInt();
        System.out.println("Please enter mine count: ");
        MINECOUNT = scanner.nextInt();
        scanner.close();
                        
        this.minesweeper = new Minesweeper(ROW, COL, MINECOUNT);

        GRID = new GridPane();

        for(int row=0;row<this.minesweeper.getROW();row++){
            for(int col=0;col<this.minesweeper.getCOL();col++){
                Button button = makeMinesweeperButton(row,col);
                GRID.add(button,row,col);
            }
        }

        GRID.setMaxHeight(48*ROW + 2*ROW);
        GRID.setMaxWidth(48*COL +2*COL );

        HBox top = new HBox();
        //Label move_count = new Label(""+int_move_count);
        VBox reset_hint = new VBox();
        
        //Reset Button
        Button reset = new Button("Reset");
        reset.setMinHeight(30);
        reset.setMinWidth(50);
        reset.setMaxHeight(30);
        reset.setMaxWidth(50);
        
        reset.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent e) {
                minesweeper = new Minesweeper(ROW, COL, MINECOUNT);

                GRID = new GridPane();
                int_move_count = 0;
                move_count.setText("" + int_move_count);

                for(int row=0;row<minesweeper.getROW();row++){
                    for(int col=0;col<minesweeper.getCOL();col++){
                        Button button = makeMinesweeperButton(row,col);
                        GRID.add(button,row,col);
                    }
                }

                GRID.setMaxHeight(48*ROW + 2*ROW);
                GRID.setMaxWidth(48*COL +2*COL );

                HBox top = new HBox();
                VBox reset_hint = new VBox();

                Button hint = new Button("Hint");
                hint.setMinHeight(30);
                hint.setMinWidth(50);
                hint.setMaxHeight(30);
                hint.setMaxWidth(50);
                hint.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        if(minesweeper.getGameState() == GameState.IN_PROGRESS|| minesweeper.getGameState() == GameState.NOT_STARTED){
                            Collection<Location> location = minesweeper.getPossibleSelections();
                            Stream<Location> locations = location.stream();
                            List<Location> finalLocation = locations.toList();
                            Location h = finalLocation.get(0);
                            ObservableList<Node> childrens = GRID.getChildren();
                            Node current = childrens.get(h.getRow()*minesweeper.getCOL() + h.getCol());
                            Button button = (Button)(current);
                            button.setGraphic(new ImageView(HINT));
                        }
                    }
                });

                reset_hint.getChildren().addAll(reset,hint);
                reset_hint.setPadding(new Insets(0,15,0,15));
                Label mine_count = new Label(""+MINECOUNT);
                

                top.getChildren().addAll(move_count,reset_hint,mine_count);
                top.setAlignment(Pos.CENTER);

                HBox bottom_text = new HBox();
                Button solve = new Button("Solve");
                solve.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent arg0) {
                        Configuration initial = new MinesweeperSolver(minesweeper);
                        Backtracker back = new Backtracker(false);
                        MinesweeperSolver solution = (MinesweeperSolver)back.solve(initial);
                        minesweeper = solution.getMinesweeper();
                        //update board helper method
                        updateBoard();
                    }
            
                    });
                text_at_bottom.setText("");
                bottom_text.getChildren().addAll(text_at_bottom,solve);
                bottom_text.setAlignment(Pos.CENTER);


                VBox vbox = new VBox();
                vbox.getChildren().addAll(top,GRID,bottom_text);
                Scene scene1 = new Scene(vbox);                
                
 
                stage.setTitle("Simple Addition");
                stage.setScene(scene1);
                stage.show();
            }
        });
       //End of Reset button

        //Hint button
        Button hint = new Button("Hint");
        hint.setMinHeight(30);
        hint.setMinWidth(50);
        hint.setMaxHeight(30);
        hint.setMaxWidth(50);
        //HintMaker hintObject = new HintMaker(this);
        hint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(minesweeper.getGameState() == GameState.IN_PROGRESS|| minesweeper.getGameState() == GameState.NOT_STARTED){
                    Collection<Location> location = minesweeper.getPossibleSelections();
                    Stream<Location> locations = location.stream();
                    List<Location> finalLocation = locations.toList();
                    Location h = finalLocation.get(0);
                    ObservableList<Node> childrens = GRID.getChildren();
                    //index = row*n+col
                    Node current = childrens.get(h.getRow()*minesweeper.getCOL() + h.getCol());
                    Button button = (Button)(current);
                    button.setGraphic(new ImageView(HINT));
                }
            }
        });
        //End of Hint button

        reset_hint.getChildren().addAll(reset,hint);
        reset_hint.setPadding(new Insets(0,15,0,15));
        Label mine_count = new Label(""+MINECOUNT);
        
        
        top.getChildren().addAll(move_count,reset_hint,mine_count);
        top.setAlignment(Pos.CENTER);

        HBox bottom_text = new HBox();
        //start of solve button
        Button solve = new Button("Solve");
        solve.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                Configuration initial = new MinesweeperSolver(minesweeper);
                Backtracker back = new Backtracker(false);
                MinesweeperSolver solution = (MinesweeperSolver)back.solve(initial);
                minesweeper = solution.getMinesweeper();
                //update board helper method
                updateBoard();
            }
            
        });
        //Dont forget to add it inside the reset eventhandler anon class when done
        bottom_text.getChildren().addAll(text_at_bottom,solve);
        bottom_text.setAlignment(Pos.CENTER);


        VBox vbox = new VBox();
        vbox.getChildren().addAll(top,GRID,bottom_text);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setTitle("Minesweeper");
        stage.show();
    }
    
    public void makeMove(Location location){
        //Check gamestate
        if(minesweeper.getGameState() == GameState.IN_PROGRESS|| minesweeper.getGameState() == GameState.NOT_STARTED){
            try{
                minesweeper.makeSelection(location);
                int_move_count++;
                move_count.setText("" + int_move_count);
            }catch(MinesweeperException e){
                System.out.println("Bad Move");
            }
        }
    }

    //Method that is attenpting to uncover the whole board when the gamestate is either WON or LOSS
    public void uncoverBoard(){
        if(minesweeper.getGameState() == GameState.WON || minesweeper.getGameState() == GameState.LOST){
            boolean b = minesweeper.getGameState() == GameState.WON;
            if(b){
                text_at_bottom.setText(wonString);
            }else{
                text_at_bottom.setText(loseString);
            }
            //First get the Location[][] board
            Location[][] board = minesweeper.getBoard();
            //Get the buttons with the same method as hint
            ObservableList<Node> childrens = GRID.getChildren();
            for(int i = 0;i < minesweeper.getROW();i++){
                for(int j = 0;j<minesweeper.getCOL();j++){
                    Location h = board[i][j];
                    Node current = childrens.get(h.getRow()*minesweeper.getCOL() + h.getCol());
                    Button button = (Button)(current);
                    //call a switch statement on every button and location combo
                    switch(h.getValue()){
                        case 0:
                            button.setGraphic(new ImageView(ZERO));
                            break;
                        case 1: 
                            button.setGraphic(new ImageView(ONE));
                            break;
                        case 2: 
                            button.setGraphic(new ImageView(TWO));
                            break;
                        case 3: 
                            button.setGraphic(new ImageView(THREE));
                            break;
                        case 4: 
                            button.setGraphic(new ImageView(FOUR));
                            break;
                        case 5: 
                            button.setGraphic(new ImageView(FIVE));
                            break;
                        case 6: 
                            button.setGraphic(new ImageView(SIX));
                            break;
                        case 7: 
                            button.setGraphic(new ImageView(SEVEN));
                            break;
                        case 8: 
                            button.setGraphic(new ImageView(EIGHT));
                            break;
                        case 9: 
                            button.setGraphic(new ImageView(MINE));
                            break;
                    }
                }
            }
        }
    }

    public void updateBoard(){
        if(minesweeper.getGameState() == GameState.WON || minesweeper.getGameState() == GameState.LOST){
            boolean b = minesweeper.getGameState() == GameState.WON;
            if(b){
                text_at_bottom.setText(wonString);
            }else{
                text_at_bottom.setText(loseString);
            }
            //First get the Location[][] board
            Location[][] board = minesweeper.getBoard();
            //Get the buttons with the same method as hint
            ObservableList<Node> childrens = GRID.getChildren();
            for(int i = 0;i < minesweeper.getROW();i++){
                for(int j = 0;j<minesweeper.getCOL();j++){
                    Location h = board[i][j];
                    Node current = childrens.get(h.getRow()*minesweeper.getCOL() + h.getCol());
                    Button button = (Button)(current);
                    //call a switch statement on every button and location combo
                    if(h.getIsCovered()){
                        button.setGraphic(new ImageView(COVERED));
                    }else{
                        switch(h.getValue()){
                            case 0:
                                button.setGraphic(new ImageView(ZERO));
                                break;
                            case 1: 
                                button.setGraphic(new ImageView(ONE));
                                break;
                            case 2: 
                                button.setGraphic(new ImageView(TWO));
                                break;
                            case 3: 
                                button.setGraphic(new ImageView(THREE));
                                break;
                            case 4: 
                                button.setGraphic(new ImageView(FOUR));
                                break;
                            case 5: 
                                button.setGraphic(new ImageView(FIVE));
                                break;
                            case 6: 
                                button.setGraphic(new ImageView(SIX));
                                break;
                            case 7: 
                                button.setGraphic(new ImageView(SEVEN));
                                break;
                            case 8: 
                                button.setGraphic(new ImageView(EIGHT));
                                break;
                            case 9: 
                                button.setGraphic(new ImageView(MINE));
                                break;
                        }
                    }
                }
            }
        }
    }
    

    public void updateButton(Button button, Location location){
        if(location.getIsCovered()){
            button.setGraphic(new ImageView(COVERED));
        }else{
        switch(location.getValue()){
                case 0:
                    button.setGraphic(new ImageView(ZERO));
                    uncoverBoard();
                    break;
                case 1: 
                    button.setGraphic(new ImageView(ONE));
                    uncoverBoard();
                    break;
                case 2: 
                    button.setGraphic(new ImageView(TWO));
                    uncoverBoard();
                    break;
                case 3: 
                    button.setGraphic(new ImageView(THREE));
                    uncoverBoard();
                    break;
                case 4: 
                    button.setGraphic(new ImageView(FOUR));
                    uncoverBoard();
                    break;
                case 5: 
                    button.setGraphic(new ImageView(FIVE));
                    uncoverBoard();
                    break;
                case 6: 
                    button.setGraphic(new ImageView(SIX));
                    uncoverBoard();
                    break;
                case 7: 
                    button.setGraphic(new ImageView(SEVEN));
                    uncoverBoard();
                    break;
                case 8: 
                    button.setGraphic(new ImageView(EIGHT));
                    uncoverBoard();
                    break;
                case 9: 
                    button.setGraphic(new ImageView(MINE));
                    uncoverBoard();
                    break;
            }
        }
    }

    private Button makeMinesweeperButton(int row,int col){
        Button minesweeperButton = new Button();
        minesweeperButton.setBackground(new Background(new BackgroundImage(COVERED,BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,
        BackgroundSize.DEFAULT)));

        Location location = minesweeper.getLocation(row,col);
        LocationChanger changer = new LocationChanger(this,minesweeperButton);
        location.register(changer);
        updateButton(minesweeperButton, location);
        minesweeperButton.setPadding(Insets.EMPTY);

        minesweeperButton.setPrefSize(72, 72);

        MoveMaker moveMaker = new MoveMaker(location,this);
        minesweeperButton.setOnAction(moveMaker);

        return minesweeperButton;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}