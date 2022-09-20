package minesweeper.model;

import java.util.ArrayList;
import java.util.Collection;

import backtracker.Backtracker;
import backtracker.Configuration;

public class MinesweeperSolver implements Configuration{
    private final Minesweeper minesweeper;

    public MinesweeperSolver(Minesweeper minesweeper){
        this.minesweeper = minesweeper;
    }

    @Override
    public Collection<Configuration> getSuccessors() {
        Minesweeper copy = minesweeper.deepcopy();
        // Location[] selList = solver.minesweeper.getPossibleSelections();
        ArrayList<Location> selList = new ArrayList<>(copy.getPossibleSelections());
        Collection<Configuration> s = new ArrayList<>();

        for (int i = 0; i < copy.getPossibleSelections().size(); i++){
            Location l = selList.get(i);

            MinesweeperSolver sln = new MinesweeperSolver(copy.deepcopy());
            try{
                sln.minesweeper.makeSelection(l);
            } catch(MinesweeperException e){
                //squash
            }

            s.add(sln);
        }
        return s;
    }

    @Override
    public boolean isValid() {

        return this.minesweeper.getGameState() != GameState.LOST;
    }


    @Override
    public boolean isGoal() {
        return this.minesweeper.getGameState() == GameState.WON;
    }

    @Override
    public String toString() {
        return this.minesweeper.toString();
    }

    public Minesweeper getMinesweeper(){
        return this.minesweeper;
    }
    
    public static void main(String[] args) {
        Minesweeper minesweeper = new Minesweeper(5, 5, 5);
        Configuration initial = new MinesweeperSolver(minesweeper);
        Backtracker back = new Backtracker(false);
        MinesweeperSolver solution = (MinesweeperSolver)back.solve(initial);
        if (back!=null){
            System.out.println(solution.toString());
        }
    }
}
