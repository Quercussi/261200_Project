package entities;

public class Tile implements Coordinated{
    private CityCrew owner ;

    private double deposit ;

    private final Position position ;
    public Tile(double deposit,Position position){
        this.owner = null ;
        this.deposit = deposit ;
        this.position = position ;

        if (this.deposit < 0) this.deposit = 0;
        else if (this.deposit > max_dep) this.deposit = max_dep;
    }

    public void setOwner(CityCrew player) { owner = player ; }

    public CityCrew getOwner(){ return owner ;}
    public double getDeposit() { return deposit; }

    public void withdraw(double withdraw) {
        this.deposit -= withdraw;
        if (this.deposit < 0) this.deposit = 0;
    }

    public void deposit(double deposit) {
        this.deposit += deposit;
        if (this.deposit > max_dep) this.deposit = max_dep;
    }

    public Position getPosition() { return position ; }

    public void updateOwnership(CityCrew crew) {
        if (deposit < 1) {
            setOwner(null);
            if (crew == null)
                return;

            crew.removeTile(this);
            if(crew.getCityCenter() == this)
                crew.setCityCenter(null);

        } else if (owner == null) {
            setOwner(crew);
            if(crew != null)
                crew.addTile(this);
        }
    }

    public static void setMax_dep(Long max_dep) {
        Tile.max_dep = max_dep;
    }

    private static long max_dep = Long.MAX_VALUE;
}
