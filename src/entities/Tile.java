package entities;

import parsers.*;
public class Tile implements Coordinated{
    private final Player owner ;

    private final double deposit ;

    private final Position position ;
    public Tile(Player owner,double deposit,Position position){
        this.owner = owner ;
        this.deposit = deposit ;
        this.position = position ;
    }

    public Player setOwner(){
        return null ;
    }

    public Player getOwner(){
        return  null ;
    }

    public boolean hasAdjacentTile(Player player){
        return  false ;
    }

    public int getRow(){ return position.getRow() ;}

    public int getCol(){
        return position.getCol() ;
    }

    public Position getPosition(){
        return position.getPosition() ;
    }

    public Position getPositionAt(Direction Dir){
        return position.getPositionAt(Dir) ;
    }

    public int distanceTo(Coordinated coordinated){
        return position.distanceTo(coordinated) ;
    }
}
