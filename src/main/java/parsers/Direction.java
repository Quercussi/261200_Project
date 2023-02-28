package parsers;

public enum Direction {
    up, down, upleft, upright, downleft, downright;

    /** Convert Direction String to Direction Enumerator
     * @param strDir is a String
     * @return value of the input string in Direction Enumerator
     * @throws SyntaxError if strDir cannot be converted to Direction Enumerator
     */
    public static Direction getDir(String strDir) throws SyntaxError {
        try { return Direction.valueOf(strDir); }
        catch (IllegalArgumentException | NullPointerException e) {
            throw new SyntaxError("Unknown direction: " + strDir, null);
        }
    }
}
