package parsers;

import java.util.Map;

public class AttackCmd implements Statement{
    private Direction dir;
    private Expression expr;

    AttackCmd(Direction dir, Expression expr) {
        this.dir = dir;
        this.expr = expr;
    }

    public void execute(Map<String, Integer> bindings) {
        switch (dir) {
            case up -> {break;}
            case down -> {break;}
            case upleft -> {break;}
            case upright -> {break;}
            case downleft -> {break;}
            case downright -> {break;}
        }
    }
}
