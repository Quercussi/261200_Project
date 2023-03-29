package orchestrator;

import entities.*;
import parsers.SyntaxError;

import java.io.FileNotFoundException;
import java.util.*;

import static orchestrator.ConfigurationReader.missingKeys;
import static orchestrator.ConfigurationReader.negativeKeyValue;

public class Upbeat {
    public enum GameState {configSetting, joining, gameStart}

    public static Territory game;
    public static List<CityCrew> crews = new ArrayList<>();
    public static List<CityCrew> losers = new ArrayList<>();
    public static Map<String,Long> config = null;

    public static State currentState;
    public static GameState gameState = GameState.configSetting;

    private static long rows;
    private static long cols;
    private static long init_budget;
    private static long init_center_dep;
    private static long rev_cost;
    private static long init_rev_time;
    private static long plan_rev_time;

    public static List<Tile> vacantTile;

    static final String BRAIN_DEAD_CONSTRUCTION_PLAN = "done";

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
        if (config == null)
            setConfig(new ConfigurationReader("main/java/orchestrator/config.txt").getConfig());

        return config;
    }

    public static void setConfig(Map<String, Long> config) throws MissingConfigurationVariable, IllegalConfiguration {
        checkConfigValidity(config);

        // All passes;
        Upbeat.config = config;
        rows = config.get("m");
        cols = config.get("n");
        init_center_dep = config.get("init_center_dep");
        init_budget = config.get("init_budget");
        rev_cost = config.get("rev_cost");
        init_rev_time = config.get("init_plan_min")*60 + config.get("init_plan_sec");
        plan_rev_time = config.get("plan_rev_min")*60 + config.get("plan_rev_sec");
    }

    public static void checkConfigValidity(Map<String,Long> config) throws MissingConfigurationVariable, IllegalConfiguration {
        // config misses keys
        Set<String> missingKeys = missingKeys(config);
        if(!missingKeys.isEmpty()){
            StringBuilder sb = new StringBuilder("Keys missing: ");
            for(String var : missingKeys)
                sb.append(var).append(' ');

            throw new MissingConfigurationVariable(sb.toString());
        }

        // config has negative values
        Set<String> negativeKeyValue = negativeKeyValue(config);
        if(!negativeKeyValue.isEmpty()) {
            StringBuilder sb = new StringBuilder("Keys with negative value: ");
            for (String key : negativeKeyValue)
                sb.append(key).append(' ');

            throw new IllegalConfiguration(sb.toString());
        }
    }

    public static void constructTerritory() {
        try { getConfig(); }
        catch (FileNotFoundException | MissingConfigurationVariable | IllegalConfiguration e) {
            System.out.println("Whoops");
        }

        Upbeat.game = new Territory(config);
        vacantTile = new ArrayList<>((int) (rows*cols));
        for(Tile[] tileRow : game.getGraph())
            Collections.addAll(vacantTile, tileRow);
    }

    // Crews and Tiles Initialization
    public static CityCrew randomizedInitCrew(String name, int id, String uuid, String sessionId) throws SyntaxError {
        Tile cityCenter = popRandomElement(vacantTile);
        if(cityCenter == null) return null;

        return initCrew(name, id,uuid, sessionId, BRAIN_DEAD_CONSTRUCTION_PLAN, cityCenter);
    }

    private static CityCrew initCrew(String name, int id, String uuid, String sessionId, String str_construction_plan, Coordinated cityCenterPos) throws SyntaxError {
        int row = cityCenterPos.getRow();
        int col = cityCenterPos.getCol();
        Tile tile = game.getGraph()[row][col];
        CityCrew crew = new CityCrew(name, id, uuid, sessionId, init_budget, tile, str_construction_plan);
        crew.setCityCenter(tile);

        tile.deposit(init_center_dep);
        tile.updateOwnership(crew);
        return crew;
    }

    public static Territory getTerritory() { return game; }

    public static void setStates() {
        game.setCrews(crews);

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

    public static long get_rev_cost() { return rev_cost; }
    public static long get_init_rev_time() { return init_rev_time; }
    public static long get_plan_rev_time() { return plan_rev_time; }

    public static CityCrew getCrewWith(int crewId) {
        CityCrew crew = null;
        for(CityCrew c : Upbeat.crews) {
            if(crewId == c.getId()) {
                crew = c;
                break;
            }
        }

        return crew;
    }

    public static CityCrew getCrewWithSession(String sessionId) {
        CityCrew crew = null;
        for(CityCrew c : Upbeat.crews) {
            if(c.correctSessionId(sessionId)) {
                crew = c;
                break;
            }
        }

        return crew;
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static final Random rand = new Random();

    private static <E> E popRandomElement(List<E> list) {
        int size = list.size();
        if (size == 0)
            return null;

        int randIdx = rand.nextInt(size);

        E poppedElement = list.get(randIdx);
        list.remove(poppedElement);

        return poppedElement;
    }
}
