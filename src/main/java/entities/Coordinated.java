package entities;

import parsers.Direction;

public interface Coordinated {
    /** Return position of the object
     * @return position of the object
     */
    Position getPosition() ;

    /** Return row of the position of the object
     * @return row of the position of the object
     */
    default int getRow() {
        return getPosition().getRow();
    }

    /** Return column of the position of the object
     * @return column of the position of the object
     */
    default int getCol() {
        return getPosition().getCol();
    }

    /** Return the position at the input direction
     * @param Dir is the direction the object is pointing to
     * @return the position at the input direction
     */
    default Position getPositionAt(Direction Dir) {
        int row = getRow();
        int col = getCol();

        switch (Dir) {
            case up -> { return new Position(row-1, col); }
            case down -> { return new Position(row+1, col); }
            case upright -> { return new Position(row-(col%2==0 ? 1 : 0),col+1); }
            case downright -> { return new Position(row+(col%2==0 ? 0 : 1),col+1); }
            case upleft -> { return new Position(row-(col%2==0 ? 1 : 0),col-1); }
            case downleft -> { return new Position(row+(col%2==0 ? 0 : 1),col-1); }
        }

        return null;
    }

    /** Calculate the distance between two coordinated object
     *
     * @param coordinated in the input coordinated object
     * @return the distance between two coordinated object
     */
    default int distanceTo(Coordinated coordinated) {
        int row1 = this.getRow();
        int col1 = this.getCol();
        int row2 = coordinated.getRow();
        int col2 = coordinated.getCol();

        // This function works by calculating the manhattan distance.
        // Then subtract the distance by the number of diagonal lines accessed.

        int manhattanDistance = Math.abs(col2-col1) + Math.abs(row2-row1);

        int maxDiagonalCount = Math.min(Math.abs(row2-row1),Math.abs(col2-col1));

        int columnDiagonalCount = Math.abs(col2-col1)/2;
        if(row2 <= row1)
            columnDiagonalCount += (col1%2 == 0) ? 1 : 0;
        else
            columnDiagonalCount += (col1%2 == 1) ? 1 : 0;

        int diagonalCount = Math.min(maxDiagonalCount, columnDiagonalCount);

        return manhattanDistance - diagonalCount;
    }
}
