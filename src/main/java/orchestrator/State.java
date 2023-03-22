package orchestrator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import entities.Alteration;
import entities.CityCrew;
import entities.Tile;
import parsers.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties({"nextState"})
public class State {
    CityCrew crew;
    State nextState;

    public State(CityCrew crew, State nextState) {
        this.crew = crew;
        this.nextState = nextState;
    }

    public List<Alteration> execute(String uuid) throws InvalidToken {
        if(!crew.correctUUID(uuid))
            throw new InvalidToken("The input token is incorrect.");

        crew.incrementTurn();

        // Add interest
        for(Tile tile : crew.getOwnedTiles())
            tile.addInterest();


        // Execute Construction Plan
        Statement stm = crew.getConstructionPlan();
        List<Alteration> alterations = new ArrayList<>();
        stm.execute(crew.getBindings(), crew, Upbeat.game, alterations);

        // skip losers
        for(CityCrew crew : Upbeat.crews)
            if(crew.getCityCenter() == null) {
                crew.resign(uuid);
            }

        while(Upbeat.losers.contains(nextState.getCrew()))
            nextState = nextState.getNextState();

        Upbeat.currentState = nextState;

        return alterations;
    }

    public void setNextState(State nextState) { this.nextState = nextState; }

    private State getNextState() { return nextState; }
    public int getNextStateCrewId() { return nextState.getCrew().getId(); }
    public String getNextStateCrewName() { return nextState.getCrew().getName(); }
    public CityCrew getCrew() { return crew; }
}
