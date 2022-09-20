package minesweeper.view;

import javafx.scene.control.Button;
import minesweeper.model.Location;
import minesweeper.model.MinesweeperObserver;

public class LocationChanger implements MinesweeperObserver{

    private final Button button;
    private final MinesweeperGUI minesweeperGUI;

    public LocationChanger(MinesweeperGUI minesweeperGUI, Button button){
        this.minesweeperGUI = minesweeperGUI;
        this.button = button;
    }

    @Override
    public void cellUpdated(Location location) {
        minesweeperGUI.updateButton(button,location);
    }
    
}
