import entities.CityCrew;
import entities.Position;
import entities.Territory;
import entities.Tile;
import parsers.StatementParser;
import parsers.SyntaxError;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Upbeat {
    private static Territory game;
    private static Collection<CityCrew> crews;
    private static Map<String,Long> config;


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

    public static void main(String[] args) throws MissingConfigurationVariable, IllegalConfiguration, IOException, SyntaxError {
        createGame();

        while(crews.size() > 1) {
            System.out.println("turn: " + game.getTurn());
            iterateCrews();
        }
    }

    private static void iterateCrews() throws SyntaxError {
       game.incrementTurn();

       for(CityCrew crew : crews) {
           // Adding interest
           for(Tile tile : crew.getOwnedTiles())
               game.addInterest(tile);

           game.execute(crew);
       }

       Collection<CityCrew> temporaryCrews = new ArrayList<>(crews);
       for(CityCrew crew : temporaryCrews) // This is to prevent ConcurrentModificationException
           if(crew.getCityCenter() == null)
               crews.remove(crew);
    }

    private static void createGame() throws IOException, MissingConfigurationVariable, IllegalConfiguration, SyntaxError {
        setConfig(getConfig());
        rows = config.get("m");
        cols = config.get("n");
        init_center_dep = config.get("init_center_dep");
        init_budget = config.get("init_budget");

        game = new Territory(config);
        setCrews();
        game.setCrews(crews);
    }

    private static Map<String,Long> getConfig() throws FileNotFoundException, MissingConfigurationVariable, IllegalConfiguration {
        ConfigurationReader configurationReader = new ConfigurationReader("config.txt");
        return configurationReader.getConfig();
    }

    private static void setConfig(Map<String,Long> config) {
        Upbeat.config = config;
    }

    private static void setCrews() throws IOException, SyntaxError {
        System.out.print("Please enter the number of players: ");
        Scanner n = new Scanner(System.in);
        int size = n.nextInt();

        crews = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            System.out.print("Enter player " + (i+1) + ": ");
            Scanner input = new Scanner(System.in);
            String name = input.next();

            Path constructionPath = Paths.get(new File("src\\plan"+(i+1)+".txt").getAbsolutePath());
            String strConstructionPlan = Files.readString(constructionPath, StandardCharsets.UTF_8);
            StatementParser sp = new StatementParser(strConstructionPlan);

            crews.add(randomizedInitCrew(name, sp));
        }
    }

    private static CityCrew initCrew(String name, StatementParser sp, Position cityCenterPos) {
        int row = cityCenterPos.getRow();
        int col = cityCenterPos.getCol();
        Tile tile = game.getGraph()[row][col];
        CityCrew crew = new CityCrew(name, init_budget, tile, sp);
        crew.setCityCenter(tile);

        tile.deposit(init_center_dep);
        tile.updateOwnership(crew);
        return crew;
    }

    private static CityCrew randomizedInitCrew(String name, StatementParser sp) {
        Set<Position> vacantPosition = new HashSet<>();
        for(CityCrew crew : crews)
            vacantPosition.add(crew.getCityCenter().getPosition());

        Position pos;
        do {
            int row = rand.nextInt((int)rows);
            int col = rand.nextInt((int)cols);
            pos = new Position(row, col);
        } while(vacantPosition.contains(pos)) ;

        return initCrew(name, sp, pos);
    }

    private static final Random rand = new Random();
}
