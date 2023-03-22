package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Position implements Coordinated {
    private static final StringBuilder sb = new StringBuilder();
    private static final Map<String,Position> instances = new HashMap<>();

    private final int row ;
    private final int col ;

    private Position(int row,int col) {
        this.row = row ;
        this.col = col ;
    }

    public static Position of(int row,int col) {
        sb.setLength(0); // clear the string builder
        sb.append(row).append(' ').append(col);

        String hashKey = sb.toString();
        Position pos = instances.get(hashKey);
        if(pos == null) {
            pos = new Position(row, col);
            instances.put(hashKey, pos);
        }
        return pos;
    }

    @JsonIgnore
    public Position getPosition(){ return this ;}


    @Override
    public int getRow() { return row; }

    @Override
    public int getCol() { return col; }

}
