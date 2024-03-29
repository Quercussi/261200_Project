package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import orchestrator.InvalidToken;
import orchestrator.Upbeat;
import parsers.Statement;
import parsers.StatementParser;
import parsers.SyntaxError;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class CityCrew implements Coordinated {
    private final String name ;
    private final int id;
    private final String uuid;
    private final String sessionId;
    private int turn = 0;
    private Position position ;
    private long budget ;
    private final Set<Tile> ownedTiles ;
    private Tile cityCenter ;
    private final Map<String,Long> bindings;
    private String str_construction_plan;
    private Statement construction_plan;
    private String color;
    private CountdownClock countdownClock;

    public static final String[] defaultColorScheme = {"#F2003C","#1F75FE","#FFD300","#66FF00","#0000CD","#FF1493","#BF00FF","#D2691E"};

    public CityCrew(String name, int id, String uuid, String sessionId, long initial_budget, Tile cityCenter, String str_construction_plan) throws SyntaxError {
        this.name = name ;
        this.id = id;
        this.position = cityCenter.getPosition() ;
        this.budget = initial_budget ;
        this.ownedTiles = new HashSet<>();
        this.ownedTiles.add(cityCenter);
        this.cityCenter = cityCenter ;
        this.bindings = new HashMap<>();
        this.str_construction_plan = str_construction_plan;
        this.construction_plan = new StatementParser(str_construction_plan).compile();

        try{
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] uuid_hash = digest.digest(uuid.getBytes(StandardCharsets.UTF_8));
        this.uuid = Base64.getEncoder().encodeToString(uuid_hash);
        byte[] sessionId_hash = digest.digest(sessionId.getBytes(StandardCharsets.UTF_8));
        this.sessionId = Base64.getEncoder().encodeToString(sessionId_hash);

        countdownClock = new CountdownClock(this, (int) (Upbeat.get_init_rev_time() * 10), uuid);
    }

    public Map<String,Long> getBindings() { return bindings; }
    @JsonIgnore
    public Statement getConstructionPlan() { return construction_plan; }
    @JsonIgnore
    public String getConstructionPlanStr() { return construction_plan == null ? "" : str_construction_plan; }
    public void setConstructionPlan(String str_construction_plan, String uuid) throws SyntaxError, InvalidToken {
        if(!correctUUID(uuid))
            throw new InvalidToken("The input token is incorrect.");

        this.str_construction_plan = str_construction_plan;
        Statement newConstructionPlan = new StatementParser(str_construction_plan).compile();
        if(newConstructionPlan.equals(this.construction_plan))
            throw new InvalidToken("Repeated construction plan");

        this.construction_plan = newConstructionPlan;
    }

    public String getName() { return name; }
    public int getId() { return id; }
    @JsonIgnore
    private String getUuid() { return uuid; }
    public long getBudget() { return budget; }
    public void withdraw(long budget) {
        this.budget -= budget;
        if(this.budget <= 0) this.budget = 0;
    }
    public void deposit(long budget) { this.budget += budget; }
    public Set<Tile> getOwnedTiles() { return ownedTiles; }
    public Tile getCityCenter() { return cityCenter; }
    public void setCityCenter(Tile cityCenter) { this.cityCenter = cityCenter; }

    public void moveTo(Coordinated position){
        this.position = position.getPosition() ;
    }

    public void addTile(Tile tile){
        ownedTiles.add(tile) ;
    }

    public void removeTile(Tile tile){
        ownedTiles.remove(tile) ;
    }

    public void incrementTurn() { turn++; }
    public int getTurn() { return turn; }

    @JsonIgnore
    public CountdownClock getCountdownClock() { return countdownClock; }
    public void setCountdownClock(CountdownClock countdownClock) { this.countdownClock = countdownClock; }

    public void startCountdown() {
        countdownClock.startCountdown();
    }
    public void stopCountdown() {
        countdownClock.stopCountdown();
    }

    public double getTimeLeft() {
        return countdownClock.getTimeLeft();
    }

    public void setColor(String color) { this.color = color; }
    public String getColor() { return color; }

    public void resign(String uuid) throws InvalidToken {
        if(!correctUUID(uuid))
            throw new InvalidToken("The input token is incorrect.");

        cityCenter = null;
        Upbeat.crews.remove(this);
        Upbeat.losers.add(this);

        for(Tile tile : getOwnedTiles())
            tile.setOwner(null);
        getOwnedTiles().clear();

        if(Upbeat.currentState.getCrew() == this)
            Upbeat.currentState.incrementState();
    }

    public boolean correctUUID(String inputUUID) {
        if (inputUUID == null) return false;
        byte[] hash = digest.digest(inputUUID.getBytes(StandardCharsets.UTF_8));
        return this.uuid.equals(Base64.getEncoder().encodeToString(hash));
    }

    public boolean correctSessionId(String inputSessionId) {
        byte[] hash = digest.digest(inputSessionId.getBytes(StandardCharsets.UTF_8));
        return this.sessionId.equals(Base64.getEncoder().encodeToString(hash));
    }

    @JsonIgnore
    public Position getPosition(){ return position ;}

    private final MessageDigest digest;
}
