package entities;

import parsers.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CityCrew implements Coordinated {
    private final String name ;
    private Position position ;
    private long budget ;
    private final Set<Tile> ownedTiles ;
    private Tile cityCenter ;
    private Map<String,Long> bindings;
    private StatementParser construction_plan ;
    public CityCrew(String name, long initial_budget, Tile cityCenter, StatementParser construction_plan) {
        this.name = name ;
        this.position = cityCenter.getPosition() ;
        this.budget = initial_budget ;
        this.ownedTiles = new HashSet<>();
        this.ownedTiles.add(cityCenter);
        this.cityCenter = cityCenter ;
        this.bindings = new HashMap<>();
        this.construction_plan = construction_plan ;
    }

    public Map<String,Long> getBindings() { return bindings; }
    public StatementParser getConstructionPlan() { return construction_plan; }
    public void setConstructionPlan(StatementParser construction_plan) { this.construction_plan = construction_plan; }

    public String getName() { return name; }
    public long getBudget() { return budget; }
    public void withdraw(long budget) {
        this.budget -= budget;
        if(this.budget <= 0) this.budget = 0;
    }
    public void deposit(long budget){
        this.budget += budget;
    }
    public Set<Tile> getOwnedTiles() { return ownedTiles; }
    public Tile getCityCenter() { return cityCenter; }
    public void setCityCenter(Tile cityCenter) { this.cityCenter = cityCenter; }

    public void moveTo(Coordinated position){ this.position = position.getPosition() ;}

    public void addTile(Tile tile){
        ownedTiles.add(tile) ;
    }

    public void removeTile(Tile tile){
        ownedTiles.remove(tile) ;
    }

    public Position getPosition(){ return position ;}

}
