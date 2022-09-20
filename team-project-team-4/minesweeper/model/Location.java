package minesweeper.model;

public class Location {
    private int row ;
    private int col;
    private int value;
    private boolean isCovered;
    private MinesweeperObserver observer;
    public Location(int row, int col, int value){
        this.col=col;
        this.row = row;
        this.value = value;
        this.isCovered = true;
    }

    public void register(MinesweeperObserver observer){
        this.observer = observer;
    }
    public void notifyObserver(){
        if(observer != null){
            observer.cellUpdated(this);
        }
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getValue(){
        return this.value;
    }

    public void incrementValue(){
        this.value = this.value + 1;
    }

    public boolean getIsCovered(){
        return this.isCovered;
    }

    public void uncover(){
        this.isCovered = false;
    }

    @Override
    public int hashCode(){
        return Integer.parseInt(Integer.toString(this.row) + Integer.toString(this.row));
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Location){
            return this.hashCode() == o.hashCode();
        }else{
            return false;
        }
    }

    private Location(Location l){
        this.col=l.getCol();
        this.row = l.getRow();
        this.value = l.getValue();
        this.isCovered = l.getIsCovered();
    }

    public Location deepcopy(){
        return new Location(this);
    }

    @Override
    public String toString(){
        if(isCovered == true){
            return "[-]";
        }else{
            return "[" + this.value + "]";
        }
    }
}
