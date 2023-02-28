package orchestrator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import entities.Alteration;
import entities.CityCrew;
import parsers.StatementParser;
import parsers.SyntaxError;

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
        StatementParser sp = crew.getConstructionPlan();
        List<Alteration> alterations = new ArrayList<>();
        sp.execute(crew.getBindings(), crew, Upbeat.game, alterations);

        for(CityCrew crew : Upbeat.crews)
            if(crew.getCityCenter() == null) {
                Upbeat.crews.remove(crew);
                Upbeat.losers.add(crew);
            }

        while(Upbeat.losers.contains(nextState.getCrew()))
            nextState = nextState.getNextState();

        Upbeat.currentState = nextState;

        return alterations;
    }

    public void setNextState(State nextState) { this.nextState = nextState; }

    private State getNextState() { return nextState; }
    public CityCrew getNextStateCrew() { return nextState.getCrew(); }
    public CityCrew getCrew() { return crew; }
}
