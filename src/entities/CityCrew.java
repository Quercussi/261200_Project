package entities;

import parsers.*;

import java.util.Set;

public class CityCrew implements Coordinated {
    private final String name ;
    private  final Position position ;
    private final long budget ;
    private final Set<Tile> ownedTiles ;
    private final  Tile cityCenter ;
    private final StatementParser construction_plan ;
    public CityCrew(String name, Position position, long budget,Tile cityCenter, Set<Tile> ownedTiles,StatementParser construction_plan){
        this.name = name ;
        this.position = position ;
        this.budget = budget ;
        this.ownedTiles = ownedTiles ;
        this.cityCenter = cityCenter ;
        this.construction_plan = construction_plan ;
    }

    public void moveTo(Direction Dir){ position.getPositionAt(Dir) ;}

    public void addTile(Tile tile){
        ownedTiles.add(tile) ;
    }

    public void removeTile(Tile tile){
        ownedTiles.remove(tile) ;
    }

    public Position getPosition(){ return position.getPosition() ;}

}
