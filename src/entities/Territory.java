package entities;


import parsers.StatementParser;
import parsers.SyntaxError;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Territory {
    private Collection<CityCrew> crews ;
    private final Tile[][] graph ;
    private final Map<String,Long> config;
    private int turn ;

    public Territory(Map<String,Long> config){
        long rows = config.get("m");
        long cols = config.get("n");
        this.graph = new Tile[(int)rows][(int)cols];
        this.crews = null;
        this.config = config;
        this.turn = 0;

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++)
                graph[i][j] = new Tile(0, new Position(i,j));
        }
    }

    public Tile getTileAt(Coordinated position) {
        int row = position.getRow();
        int col = position.getCol();

        if(row < 0 || row >= config.get("m") || col < 0 || col >= config.get("n"))
            return null;

        return graph[position.getRow()][position.getCol()] ;
    }

    public void execute(CityCrew crew) throws SyntaxError {
        StatementParser sp = crew.getConstructionPlan();
        //sp.execute(new HashMap<String,Long>(), crew, this);
    }

    public Tile[][] getGraph(){ return graph ;}
    public Collection<CityCrew> getCrews(){ return crews ;}
    public void setCrews(Collection<CityCrew> crews) { this.crews = crews; }
    public double getMaxDeposit() { return config.get("max_dep"); }
    public long getRows() { return config.get("m"); }
    public long getCols() {return config.get("n"); }

    public int getTurn(){
        return turn;
    }
    public void incrementTurn() { turn++; }

    public double getInterestRate(Tile tile) { return config.get("interest_pct") * Math.log10(tile.getDeposit()) * Math.log(turn); }
    public void addInterest(Tile tile) { tile.deposit(tile.getDeposit() * getInterestRate(tile) / 100); }
}
