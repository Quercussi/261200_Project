package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Territory {
    private List<CityCrew> crews ;
    private final Tile[][] graph ;
    private final Map<String,Long> config;

    public Territory(Map<String,Long> config){
        long rows = config.get("m");
        long cols = config.get("n");
        long max_dep = config.get("max_dep");
        this.graph = new Tile[(int)rows][(int)cols];
        this.crews = null;
        this.config = config;

        Tile.setMax_dep(max_dep);

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++)
                graph[i][j] = new Tile(0, Position.of(i, j));
        }
    }

    @JsonIgnore
    public Tile getTileAt(Coordinated position) {
        int row = position.getRow();
        int col = position.getCol();
        long rows = config.get("m");
        long cols = config.get("n");

        if(row < 1 || row > rows || col < 1 || col > cols)
            return null;

        return graph[position.getRow()-1][position.getCol()-1] ;
    }

    public Tile[][] getGraph(){ return graph ;}
    public List<CityCrew> getCrews(){ return crews ;}
    public void setCrews(List<CityCrew> crews) { this.crews = crews; }
    public double getMaxDeposit() { return config.get("max_dep"); }
    public long getRows() { return config.get("m"); }
    public long getCols() {return config.get("n"); }
}
