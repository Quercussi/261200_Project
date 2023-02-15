import parsers.StatementParser;
import parsers.SyntaxError;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Upbeat {

    public static void main(String[] args) throws MissingConfigurationFile, MissingConfigurationVariable, IllegalConfiguration, IOException, SyntaxError {
        ConfigurationReader configurationReader = new ConfigurationReader("config.txt");

        Map<String,Integer> config = configurationReader.getConfig();

        Map<String,Integer> integerMap = new HashMap<>();

        Path constructionPath = Paths.get(new File("src\\testConstructionPlan.txt").getAbsolutePath());
        String strConstructionPlan = Files.readString(constructionPath, StandardCharsets.UTF_8);
        System.out.println(strConstructionPlan);
        StatementParser sp = new StatementParser(strConstructionPlan);
        sp.execute(integerMap);
        for(String key : integerMap.keySet())
            System.out.println(key + " -> " + integerMap.get(key));
    }
}
