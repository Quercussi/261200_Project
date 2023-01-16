package parsers;

public enum Direction {
    up, down, upleft, upright, downleft, downright;

    public static Direction getDir(String strDir) throws SyntaxError {
        try { return Direction.valueOf(strDir); }
        catch (IllegalArgumentException | NullPointerException e) {
            throw new SyntaxError("Unknown direction: " + strDir);
        }
    }
}
