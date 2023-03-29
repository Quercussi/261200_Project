package orchestrator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import entities.CityCrew;
import entities.CountdownClock;
import entities.Territory;
import entities.Tile;
import parsers.Statement;

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

    public Territory execute(String uuid) throws InvalidToken {
        if(!crew.correctUUID(uuid))
            throw new InvalidToken("The input token is incorrect.");

        crew.incrementTurn();

        // Add interest
        for(Tile tile : crew.getOwnedTiles())
            tile.addInterest();


        // Execute Construction Plan
        Statement stm = crew.getConstructionPlan();
        stm.execute(crew.getBindings(), crew, Upbeat.game);

        incrementState();

        if(crew.getTurn() == 1)
            crew.setCountdownClock(new CountdownClock(crew, (int) (Upbeat.get_plan_rev_time() * 10), uuid));

        return Upbeat.game;
    }

    public void incrementState() {
        // skip losers
        List<CityCrew> removingCrews = new ArrayList<>();
        for(CityCrew crew : Upbeat.crews) {
            if (crew.getCityCenter() == null) {
                // Risky Resign
                removingCrews.add(crew);
                for (Tile tile : crew.getOwnedTiles()) {
                    if (tile == null)
                        continue;
                    tile.setOwner(null);
                }
                crew.getOwnedTiles().clear();
                crew.stopCountdown();

                if(Upbeat.currentState.getCrew() == crew)
                    Upbeat.currentState.incrementState();
            }
        }

        Upbeat.crews.removeAll(removingCrews);
        Upbeat.losers.addAll(removingCrews);

        while(Upbeat.losers.contains(nextState.getCrew()))
            nextState = nextState.getNextState();

        Upbeat.currentState = nextState;

        crew.stopCountdown();
        if(nextState.crew != crew || !Upbeat.crews.contains(nextState.getNextState().crew))
            nextState.crew.startCountdown();
    }

    public void setNextState(State nextState) { this.nextState = nextState; }

    private State getNextState() { return nextState; }
    public int getNextStateCrewId() { return nextState.getCrew().getId(); }
    public String getNextStateCrewName() { return nextState.getCrew().getName(); }
    public CityCrew getCrew() { return crew; }
}
