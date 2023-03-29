package orchestrator;

import entities.Alteration;
import parsers.Statement;
import parsers.StatementParser;
import parsers.SyntaxError;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ConfigurationReader {
    private final Map<String,Long> config;

    public ConfigurationReader(String fileName) throws FileNotFoundException, MissingConfigurationVariable, IllegalConfiguration {
        config = compileConfig(getStrConfig(fileName));
    }

    public Map<String,Long> getConfig() { return config; }

    private String getStrConfig(String fileName) throws FileNotFoundException {
        String strDirectory = "src\\" + fileName;
        Charset charset = StandardCharsets.UTF_8;

        String strConfig;

        try {
            File configFile = new File(strDirectory);
            Path configPath = Paths.get(configFile.getAbsolutePath());

            strConfig = Files.readString(configPath,charset);

        } catch (InvalidPathException | SecurityException | IOException | OutOfMemoryError e) { throw new FileNotFoundException(e.getMessage()); }

        return strConfig;
    }

    public static final Set<String> defaultConfigs = Set.of("m","n","init_plan_min","init_plan_sec","init_budget",
            "init_center_dep","plan_rev_min","plan_rev_sec",
            "rev_cost","max_dep","interest_pct");
    private Map<String,Long> compileConfig(String strConfig) throws IllegalConfiguration, MissingConfigurationVariable {
        Map<String,Long> config = new HashMap<>();

        try {
            Statement configStatement = new StatementParser(strConfig).compile();
            configStatement.execute(config,null,null);

        } catch (SyntaxError e) { throw new IllegalConfiguration(e.getMessage()); }

        config.remove("done");
        Upbeat.checkConfigValidity(config);

        return config;
    }

    public static Set<String> missingKeys(Map<String, Long> config) {
        Set<String> remainingKey = new HashSet<>(defaultConfigs);
        Set<String> configKeys = config.keySet();
        remainingKey.removeAll(configKeys);
        return remainingKey;
    }

    public static Set<String> negativeKeyValue(Map<String,Long> config) {
        Set<String> negativeKeyValues = new HashSet<>();
        for(String key : config.keySet()) {
            Long var = config.get(key);
            if(var == null || var < 0)
                negativeKeyValues.add(key);
        }

        return negativeKeyValues;
    }
}
