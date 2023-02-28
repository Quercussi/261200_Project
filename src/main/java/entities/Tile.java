package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Tile implements Coordinated{
    private CityCrew owner ;

    private double deposit ;

    private final Position position ;
    public Tile(double deposit,Position position){
        this.owner = null ;
        this.deposit = deposit ;
        this.position = position ;
    }

    public void setOwner(CityCrew player) { owner = player ; }

    @JsonIgnore
    public CityCrew getOwner(){ return owner ;}
    public Integer getOwnerId() { return (owner == null) ? null : owner.getId(); }
    public double getDeposit() { return deposit; }
    public void withdraw(double withdraw) { this.deposit -= withdraw; }
    public void deposit(double deposit) { this.deposit += deposit; }

    @JsonIgnore
    public Position getPosition() { return position ; }

    public void updateOwnership(CityCrew crew) {
        if (deposit < 1) {
            setOwner(null);
            if (crew == null) {

                return;
            }

            crew.removeTile(this);
            if(crew.getCityCenter() == this)
                crew.setCityCenter(null);

        } else if (owner == null) {
            setOwner(crew);
            if(crew != null)
                crew.addTile(this);
        }
    }
}
