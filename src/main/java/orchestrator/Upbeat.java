package orchestrator;

import entities.*;
import parsers.StatementParser;
import parsers.SyntaxError;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static orchestrator.ConfigurationReader.missingKeys;
import static orchestrator.ConfigurationReader.negativeKeyValue;

public class Upbeat {
    public static Territory game;
    public static List<CityCrew> crews;
    public static List<CityCrew> losers = new ArrayList<>();
    public static Map<String,Long> config;

    public static State currentState;

    private static long rows;
    private static long cols;
    private static long init_budget;
    private static long init_center_dep;

//    m=20
//    n=15
//    init_plan_min=5
//    init_plan_sec=0
//    init_budget=10000
//    init_center_dep=100
//    plan_rev_min=30
//    plan_rev_sec=0
//    rev_cost=100
//    max_dep=1000000
//    interest_pct=5


    // Configuration Initialization

    public static Map<String,Long> getConfig() throws FileNotFoundException, MissingConfigurationVariable, IllegalConfiguration {
        ConfigurationReader configurationReader = new ConfigurationReader("main/java/orchestrator/config.txt");
        return configurationReader.getConfig();
    }

    public static void setConfig(Map<String, Long> config) throws MissingConfigurationVariable, IllegalConfiguration {
        checkConfigValidity(config);

        // All passes;
        Upbeat.config = config;

        rows = config.get("m");
        cols = config.get("n");
        init_center_dep = config.get("init_center_dep");
        init_budget = config.get("init_budget");
    }

    public static void checkConfigValidity(Map<String,Long> config) throws MissingConfigurationVariable, IllegalConfiguration {
        // config misses keys
        Set<String> missingKeys = missingKeys(config);
        if(!missingKeys.isEmpty()){
            StringBuilder sb = new StringBuilder("Keys missing: ");
            for(String var : missingKeys)
                sb.append(var);

            throw new MissingConfigurationVariable(sb.toString());
        }

        // config has negative values
        Set<String> negativeKeyValue = negativeKeyValue(config);
        if(!negativeKeyValue.isEmpty()) {
            StringBuilder sb = new StringBuilder("Keys with negative value: ");
            for (String key : negativeKeyValue)
                sb.append(key);

            throw new IllegalConfiguration(sb.toString());
        }
    }


    // Crews and Tiles Initialization

    public static void ConstructGame(List<String> nameList) throws SyntaxError {
        game = new Territory(config);

        int count = 0;
        crews = new ArrayList<>();
        for(String name : nameList) {
            crews.add(randomizedInitCrew(name,count));
            count++;
        }

        game.setCrews(crews);
        setStates();
    }

    private static CityCrew randomizedInitCrew(String name, int id) throws SyntaxError {
        Set<Position> vacantPosition = new HashSet<>();
        for(CityCrew crew : crews)
            vacantPosition.add(crew.getCityCenter().getPosition());

        Position pos;
        do {
            int row = rand.nextInt((int)rows);
            int col = rand.nextInt((int)cols);
            pos = new Position(row, col);
        } while(positionOverlap(vacantPosition,pos)) ;

        return initCrew(name, id,new StatementParser("done"), pos);
    }

    private static CityCrew initCrew(String name, int id, StatementParser sp, Position cityCenterPos) {
        int row = cityCenterPos.getRow();
        int col = cityCenterPos.getCol();
        Tile tile = game.getGraph()[row][col];
        CityCrew crew = new CityCrew(name, id, init_budget, tile, sp);
        crew.setCityCenter(tile);

        tile.deposit(init_center_dep);
        tile.updateOwnership(crew);
        return crew;
    }

    public static Territory getTerritory() { return game; }

    private static void setStates() {
        State[] states = new State[crews.size()];

        int statesCount = states.length;

        // Construct states
        for(int i = 0; i < statesCount; i++)
            states[i] = new State(crews.get(i),null);

        // Set next states
        for(int i = 0; i < statesCount-1; i++)
            states[i].setNextState(states[i+1]);

        int lastIndex = statesCount-1;
        states[lastIndex].setNextState(states[0]);

        currentState = states[0]; // First State
    }

    private static boolean positionOverlap(Set<Position> settledPositions, Position position) {
        for(Position settledPosition : settledPositions) {
            if (settledPosition.getRow() == position.getRow() && settledPosition.getCol() == position.getCol())
                return true;
        }
        return false;
    }

    private static final Random rand = new Random();
}
