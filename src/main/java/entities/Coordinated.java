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
        int row = getRow()+1;
        int col = getCol()+1;

        switch (Dir) {
            case up -> row--;
            case down -> row++;
            case upright -> {
                row -= (col%2==0 ? 1 : 0);
                col++;
            }
            case downright -> {
                row += (col%2==0 ? 0 : 1);
                col++;
            }
            case upleft -> {
                row -= (col%2==0 ? 1 : 0);
                col--;
            }
            case downleft -> {
                row += (col%2==0 ? 0 : 1);
                col--;
            }
        }

        return Position.of(row-1,col-1);
    }

    /** Calculate the distance between two coordinated object
     *
     * @param coordinated in the input coordinated object
     * @return the distance between two coordinated object
     */
    default int distanceTo(Coordinated coordinated) {
        int row1 = this.getRow()+1;
        int col1 = this.getCol()+1;
        int row2 = coordinated.getRow()+1;
        int col2 = coordinated.getCol()+1;

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
