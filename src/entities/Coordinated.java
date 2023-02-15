package entities;

import parsers.Direction;

public interface Coordinated {
    int getRow() ;

    int getCol() ;

    Position getPosition() ;

    Position getPositionAt(Direction Dir) ;

    int distanceTo(Coordinated coordinated) ;
}
