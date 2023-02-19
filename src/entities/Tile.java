package entities;

import parsers.*;
public class Tile implements Coordinated{
    private CityCrew owner ;

    private final double deposit ;

    private final Position position ;
    public Tile(CityCrew owner,double deposit,Position position){
        this.owner = owner ;
        this.deposit = deposit ;
        this.position = position ;
    }

    public CityCrew setOwner(CityCrew player){
        owner = player ;
        return owner ;
    }

    public CityCrew getOwner(){ return  owner ;}

    public boolean hasAdjacentTile(CityCrew player) {
        int rowplayer = player.getRow();
        int colplayer = player.getCol();
        int rowowner = owner.getRow();
        int colowner = owner.getCol();

        int nearbyY = Math.abs(rowowner - rowplayer);
        int nearbyX = Math.abs(colowner - colplayer);

        if(colowner % 2 == 0){
            if(nearbyY == 1 && nearbyX == 1) return true; // upleft,upright,downleft,downright
            if(nearbyY == 1 && nearbyX == 0) return true; // up,down
        }else{
            if(nearbyY == 0 && nearbyX == 1) return true; // upleft,upright
            if(nearbyY == 1 && nearbyX == 0) return true ; // up,down
            if(nearbyY == 1 && nearbyX == 1) return true; // downleft,downright
        }

        return false ;
    }

    public Position getPosition(){
        return position.getPosition() ;
    }


}
