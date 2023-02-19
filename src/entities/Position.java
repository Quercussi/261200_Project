package entities;

import parsers.Direction;

public class Position implements Coordinated {
    private final int row ;
    private final int col ;
    public Position(int row,int col){
        this.row = row ;
        this.col = col ;
    }

    public Position getPosition(){ return new Position(row,col) ;}

}
