package entities;


import java.util.Collection;

public class Territory {
    private final int cols ;
    private final int rows ;
    private final Collection<CityCrew> graph ;

    private final Tile[][] tile ;
    private final int turn ;

    public  Territory(int cols, int rows, Collection<CityCrew> graph, Tile[][] tile, int turn ){
        this.cols = cols ;
        this.rows = rows ;
        this.graph = graph ;
        this.tile = tile ;
        this.turn = turn ;
    }

    public Tile getTileAt(Position position){ return tile[position.getRow()][position.getCol()] ;}

    public Tile getTileAt(CityCrew cityCrew){
        return null ;
    }

    public Tile[][] getGraph(){ return tile ;}

    public Coordinated getCrews(){
        return null ;
    }

    public int getTurn(){
        return turn ;
    }
}
