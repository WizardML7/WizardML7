package minesweeper.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class MinesweeperTest {
    //test all gamestates click bomb = lost

    //nothing below 0 or 9
    // number of bombs = number of 9's on the board
    // possible select

    //Tests if the game gets proper mines, rows, and cols
    @Test
    public void GameSetsUpCorrect(){
        //setup
        int row = 3;
        int col = 3;
        int mineCount = 3;

        //invoke
        Minesweeper m = new Minesweeper(row, col, mineCount);

        // analyze
        assertEquals(row, m.getROW());
        assertEquals(col, m.getCOL());
        assertEquals(mineCount, m.getMineCount());
    }

    @Test
    public void BelowZero(){
        //setup
        int row = 3;
        int col = 3;
        int mineCount = 3;

        //invoke
        Minesweeper m = new Minesweeper(row, col, mineCount);

        // analyze
        int total= 0;
        Location [][] b = m.getBoard();

        for(int i=0; i<row; i++) {
            for(int j=0; j<col; j++){
                if (b[i][j].getValue()>=0){
                    total++;
                }
            }
        }
        assertEquals(total, m.getROW() * m.getCOL());
    }

    @Test
    public void AboveNine(){
        //setup
        int row = 3;
        int col = 3;
        int mineCount = 3;

        //invoke
        Minesweeper m = new Minesweeper(row, col, mineCount);

        // analyze
        int total= 0;
        Location [][] b = m.getBoard();

        for(int i=0; i<row; i++) {
            for(int j=0; j<col; j++){
                if (b[i][j].getValue()<10){
                    total++;
                }
            }
        }
        assertEquals(total, m.getROW() * m.getCOL());
    }

    @Test
    public void GameNotStarted(){
        //setup
        int row = 3;
        int col = 3;
        int mineCount = 3;

        //invoke
        Minesweeper m = new Minesweeper(row, col, mineCount);

        // analyze
        
        assertEquals(GameState.NOT_STARTED, m.getGameState());
    }

    @Test
    public void GameInProgress(){
        //setup
        int row = 3;
        int col = 3;
        int mineCount = 7;

        //invoke
        Minesweeper m = new Minesweeper(row, col, mineCount);

        // analyze
        Collection<Location> cl = m.getPossibleSelections();
        Stream<Location> sl = cl.stream();
        List<Location> l = sl.toList();
        try{
        m.makeSelection(l.get(0));
        }catch(MinesweeperException e){
            System.out.println("Bad board");
        }
        assertEquals(GameState.IN_PROGRESS, m.getGameState());
    }

    @Test
    public void GameWon(){
        //setup
        int row = 3;
        int col = 3;
        int mineCount = 8;

        //invoke
        Minesweeper m = new Minesweeper(row, col, mineCount);

        // analyze
        Collection<Location> cl = m.getPossibleSelections();
        Stream<Location> sl = cl.stream();
        List<Location> l = sl.toList();
        try{
        m.makeSelection(l.get(0));
        }catch(MinesweeperException e){
            System.out.println("Bad board");
        }
        assertEquals(GameState.WON, m.getGameState());
    }
    @Test
    public void GameLost(){
        //setup
        int row = 3;
        int col = 3;
        int mineCount = 9;

        //invoke
        Minesweeper m = new Minesweeper(row, col, mineCount);

        // analyze
        Location l = new Location(0, 0, 0);
        try{
        m.makeSelection(l);
        }catch(MinesweeperException e){
        System.out.println("Bad board");
        }
        assertEquals(GameState.LOST, m.getGameState());
    }

    @Test
    public void deepcopy(){
        int row = 3;
        int col = 3;
        int mineCount = 9;

        //invoke
        Minesweeper m = new Minesweeper(row, col, mineCount);
        Minesweeper s = m.deepcopy();

        // analyze
        try{
            s.makeSelection(new Location(0,0,4));
        } catch(MinesweeperException e){
            System.out.println("Bad Board");
        }
        boolean og = m.getLocation(0, 0).getIsCovered(); // should be true because 0,0 is uncovered in the original
        boolean copy = s.getLocation(0, 0).getIsCovered(); // should be false because we uncovered 0,0 in the deepcopy
        assertEquals(!og, copy);

    }

}
