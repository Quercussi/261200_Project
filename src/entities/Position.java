package entities;

public class Position implements Coordinated {
    private final int row ;
    private final int col ;
    public Position(int row,int col){
        this.row = row ;
        this.col = col ;
    }

    public Position getPosition(){ return this ;}

    @Override
    public int getRow() { return row; }

    @Override
    public int getCol() { return col; }

}
