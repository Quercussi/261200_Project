package entities;

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

    public CityCrew getOwner(){ return owner ;}
    public double getDeposit() { return deposit; }
    public void withdraw(double withdraw) { this.deposit -= withdraw; }
    public void deposit(double deposit) { this.deposit += deposit; }

    public Position getPosition() { return position ; }

    public void updateOwnership(CityCrew crew) {
        if (deposit < 1) {
            setOwner(null);
            if(crew != null)
                crew.removeTile(this);

        } else if (owner == null) {
            setOwner(crew);
            if(crew != null)
                crew.addTile(this);
        }
    }
}
