package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import orchestrator.Upbeat;
import parsers.StatementParser;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class CityCrew implements Coordinated {
    private final String name ;
    private final int id;
    private final String uuid;
    private int turn = 0;
    private Position position ;
    private long budget ;
    private final Set<Tile> ownedTiles ;
    private Tile cityCenter ;
    private final Map<String,Long> bindings;
    private StatementParser construction_plan;
    public CityCrew(String name, int id, String uuid, long initial_budget, Tile cityCenter, StatementParser construction_plan) {
        this.name = name ;
        this.id = id;
        this.position = cityCenter.getPosition() ;
        this.budget = initial_budget ;
        this.ownedTiles = new HashSet<>();
        this.ownedTiles.add(cityCenter);
        this.cityCenter = cityCenter ;
        this.bindings = new HashMap<>();
        this.construction_plan = construction_plan ;

        try{
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] hash = digest.digest(uuid.getBytes(StandardCharsets.UTF_8));
        this.uuid = Base64.getEncoder().encodeToString(hash);
    }

    public Map<String,Long> getBindings() { return bindings; }
    @JsonIgnore
    public StatementParser getConstructionPlan() { return construction_plan; }
    public String getConstructionPlanStr() { return construction_plan == null ? "" : construction_plan.getPlan(); }
    public void setConstructionPlan(StatementParser construction_plan) { this.construction_plan = construction_plan; }

    public String getName() { return name; }
    public int getId() { return id; }
    @JsonIgnore
    public String getUuid() { return uuid; }
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

    public void incrementTurn() { turn++; }
    public int getTurn() { return turn; }

    public void resign() {
        if(getCityCenter() == null) {
            Upbeat.crews.remove(this);
            Upbeat.losers.add(this);

            for(Tile tile : getOwnedTiles())
                tile.setOwner(null);
        }
    }

    public boolean correctUUID(String inputUUID) {
        byte[] hash = digest.digest(inputUUID.getBytes(StandardCharsets.UTF_8));
        return this.uuid.equals(Base64.getEncoder().encodeToString(hash));
    }

    @JsonIgnore
    public Position getPosition(){ return position ;}

    private final MessageDigest digest;
}
