package minesweeper.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Minesweeper{
    public final char MINE = 'M';
    public final char COVERED = '-';
    private int moveCount;
    private GameState state;
    private Location[][] board;
    private final int ROW;
    private final int COL;
    private final int MINECOUNT;
    
    //constructs a Minesweeper object with rows x columns cells and mineCount randomly placed mines.
    public Minesweeper(int row, int col, int mineCount){
        this.ROW =row;
        this.COL = col;
        this.MINECOUNT = mineCount;
        this.state = GameState.NOT_STARTED;
        
        //Creating an Array of Random Bomb Locations
        Random rng = new Random();
        this.board = new Location[row][col];

        Set<Integer> bombs = new HashSet<>();
        while(bombs.size() < MINECOUNT) {
            bombs.add(rng.nextInt(row * col));
        }
        int count = 0;
        for(int i=0; i<row; i++) {
            for(int j=0; j<col; j++){
                if(bombs.contains(count)){
                    board[i][j] = new Location(i,j,9);
                }else{
                    board[i][j] = new Location(i,j,0);
                }
                count++;
            }
        }
        for(int i=0; i<row; i++) {
            for(int j=0; j<col; j++){
                if(board[i][j].getValue() == 9){
                    
                    if(exists(i-1, j-1)){
                        if(board[i-1][j-1].getValue()!=9){
                            board[i-1][j-1].incrementValue();
                        }
                    }
                    if(exists(i, j-1)){
                        if(board[i][j-1].getValue()!=9){
                            board[i][j-1].incrementValue();
                        }
                    }
                    if(exists(i+1, j-1)){
                        if(board[i+1][j-1].getValue()!=9){
                            board[i+1][j-1].incrementValue();
                        }
                    }
                    if(exists(i+1, j)){
                        if(board[i+1][j].getValue()!=9){
                            board[i+1][j].incrementValue();
                        }
                    }
                    if(exists(i+1, j+1)){
                        if(board[i+1][j+1].getValue()!=9){
                            board[i+1][j+1].incrementValue();
                        }
                    }
                    if(exists(i, j+1)){
                        if(board[i][j+1].getValue()!=9){
                            board[i][j+1].incrementValue();
                        }
                    }
                    if(exists(i-1, j+1)){
                        if(board[i-1][j+1].getValue()!=9){
                            board[i-1][j+1].incrementValue();
                        }
                    }
                    if(exists(i-1, j)){
                        if(board[i-1][j].getValue()!=9){
                            board[i-1][j].incrementValue();
                        }
                    }
                }
            }
            
        }
    }

    public boolean exists(int row, int col){
        if( row < 0|| row >= ROW || col < 0 || col>=COL ){
            return false;
        }else{
            return true;

        }
    }

    // attempts to select a location on the board. If the selection is invalid, a MinesweeperException is thrown.
    public void makeSelection(Location location) throws MinesweeperException{
        //Where game state and the model will be changed by uncovering value beneath location
        if(location.getRow() < 0 || location.getRow() >= ROW || location.getCol() < 0 || location.getCol() >= COL) {
            throw new MinesweeperException("Invalid coordinates: (" + location.getRow() + ", " 
                + location.getCol() + ")");
        }

        if(board[location.getRow()][location.getCol()].getIsCovered() == false){
            throw new MinesweeperException("Already Uncovered!");
        }
        
        this.moveCount++;
        //check gamestate
        if(this.state == GameState.IN_PROGRESS || this.state == GameState.NOT_STARTED){
            if(board[location.getRow()][location.getCol()].getValue() == 9){
                board[location.getRow()][location.getCol()].uncover();
                System.out.println("YOU LOST");
                this.state = GameState.LOST;
                location.notifyObserver();
                //Stop the game after the losing condition is met. 
            }else if(this.moveCount == (ROW * COL) - this.MINECOUNT){
                board[location.getRow()][location.getCol()].uncover();
                this.state = GameState.WON;
                System.out.println("YOU WIN");
                location.notifyObserver();
                //Stop the game after the win condition is met.
            }else{
                board[location.getRow()][location.getCol()].uncover();
                this.state = GameState.IN_PROGRESS;
                location.notifyObserver();
            }
        }
    }

    //returns the number of moves for this game thus far.
    public int getMoveCount(){
        return this.moveCount;
    }

    //returns the current state of the game
    public GameState getGameState(){
        return this.state;
    }

    //returns a Collection of all valid cell locations, i.e. covered and save, that a player may choose from on the board with its
    //current configuration
    public Collection<Location> getPossibleSelections(){
        // go thru double for loop and add each cell to list if not a mine (!=9) and if covered
        List<Location> n = new ArrayList<>();

        for(int i=0; i<ROW; i++) {
            for(int j=0; j<COL; j++){
                if(board[i][j].getValue()!=9 && board[i][j].getIsCovered()){
                    n.add(board[i][j]);
                }
            }
        }
        return n;
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(int i=0; i<ROW; i++) {
            for(int j=0; j<COL; j++){
                s.append("[");
                if(board[i][j].getIsCovered()){
                    s.append(String.format("%c", '-'));
                }else if (!board[i][j].getIsCovered()){
                    if(board[i][j].getValue() == 9){
                        s.append(String.format("%c", 'M'));
                    }else{
                        s.append(String.format("%d", board[i][j].getValue()));
                    }
                }
                s.append("]");
            }
            s.append("\n");
        }
        return s.toString();
    }

    public int getROW() {
        return ROW;
    }

    public int getCOL() {
        return COL;
    }

    public int getMineCount() {
        return MINECOUNT;
    }

    public Location[][] getBoard() {
        return board;
    }

    public Location getLocation(int row, int col){
        Location[][] board = getBoard();
        return board[row][col];
    }

    public char getSymbol(Location location)throws MinesweeperException{
        //returns a character that represents the state of a cell at the given location. 
        //The character must be one of Minesweeper.COVERED, Minesweeper.MINE, or ‘0’ - ‘8’ (number of adjacent mines)..
        return 'c';
    }

    public boolean isCovered(Location location){
        // returns true if the cell at the given location is covered and false otherwise.
        return location.getIsCovered();
    }

    private  Minesweeper (Minesweeper original){
        this.ROW = original.getROW();
        this.COL = original.getCOL();
        this.MINECOUNT = original.getMineCount();
        this.moveCount = original.getMoveCount();
        this.state = original.getGameState();

        // this.board = Arrays.copyOf(original.getBoard(), );
        this.board = new Location[this.ROW][this.COL];
        // for (int i =0;i<this.ROW;i++){
            
        //     Location f = original.getBoard()[i][this.COL];
        //     Location l = f.deepcopy();
        //     original.getBoard()[i][this.COL] = l;
        //     this.board[i] = Arrays.copyOf(original.getBoard()[i], this.COL);
        // }

        for (int i =0;i<this.ROW;i++){
            for (int j =0;j<this.COL;j++){
                Location f = original.getBoard()[i][j];
                Location l = f.deepcopy();
                this.board[i][j] = l;
            }
        }

        // this.board = new Location[this.ROW][this.COL];
        // for(int i = 0;i < this.ROW;i++){
        //     this.board[i] = Arrays.copyOf(original.board[i], this.ROW);
        //     for (int j =0;j<this.COL;j++){
        //         this.board[j] = Arrays.copyOf(original.board[j], this.COL);
        //     }
        // }

        //This.moves = original.moves
    }

    public Minesweeper deepcopy(){
        return new Minesweeper(this);
    }
    
}
