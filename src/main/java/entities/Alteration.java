package entities;

public class Alteration {
    private final CityCrew crew;
    private final Tile tile;
    private final ActionType actionType;

    public Alteration(CityCrew crew, Tile tile, ActionType actionType) {
        this.crew = crew;
        this.tile = tile;
        this.actionType = actionType;
    }

    public CityCrew getCrew() { return crew; }
    public Tile getTile() { return tile; }
    public ActionType getActionType() { return actionType; }

    public enum ActionType {relocate, move, invest, collect, shoot}
}
