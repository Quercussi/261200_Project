package orchestrator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import entities.Alteration;
import entities.CityCrew;
import entities.Tile;
import parsers.StatementParser;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"nextState"})
public class State {
    CityCrew crew;
    State nextState;

    public State(CityCrew crew, State nextState) {
        this.crew = crew;
        this.nextState = nextState;
    }

    public List<Alteration> execute() {
        crew.incrementTurn();

        // Add interest
        for(Tile tile : crew.getOwnedTiles())
            tile.addInterest();


        // Execute Construction Plan
        StatementParser sp = crew.getConstructionPlan();
        List<Alteration> alterations = new ArrayList<>();
        sp.execute(crew.getBindings(), crew, Upbeat.game, alterations);

        // skip losers
        for(CityCrew crew : Upbeat.crews)
            if(crew.getCityCenter() == null) {
                crew.resign();
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
