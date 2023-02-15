package entities;

import parsers.*;
public class Tile implements Coordinated{
    private final CityCrew owner ;

    private final double deposit ;

    private final Position position ;
    public Tile(CityCrew owner,double deposit,Position position){
        this.owner = owner ;
        this.deposit = deposit ;
        this.position = position ;
    }

    public CityCrew setOwner(){
        return null ;
    }

    public CityCrew getOwner(){
        return  null ;
    }

    public boolean hasAdjacentTile(CityCrew player){
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
