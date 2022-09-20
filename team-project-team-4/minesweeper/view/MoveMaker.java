package minesweeper.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import minesweeper.model.Location;

public class MoveMaker implements EventHandler<ActionEvent>{
    private final Location location;
    private final MinesweeperGUI minesweeperGUI;

    public MoveMaker(Location location,MinesweeperGUI minesweeperGUI){
        this.location = location;
        this.minesweeperGUI = minesweeperGUI;
    }

    @Override
    public void handle(ActionEvent event) {
        minesweeperGUI.makeMove(location);
    }
}
