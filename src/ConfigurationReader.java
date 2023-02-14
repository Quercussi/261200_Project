import parsers.StatementParser;
import parsers.SyntaxError;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConfigurationReader {
    private final Map<String,Integer> config;

    public ConfigurationReader(String fileName) throws MissingConfigurationFile, MissingConfigurationVariable, IllegalConfiguration {
        config = compileConfig(getStrConfig(fileName));
    }

    public Map<String,Integer> getConfig() { return config; }

    private String getStrConfig(String fileName) throws MissingConfigurationFile {
        String strDirectory = "src\\" + fileName;
        Charset charset = StandardCharsets.UTF_8;

        String strConfig;

        try {
            File configFile = new File(strDirectory);
            Path configPath = Paths.get(configFile.getAbsolutePath());

            strConfig = Files.readString(configPath,charset);

        } catch (InvalidPathException | SecurityException | IOException | OutOfMemoryError e) { throw new MissingConfigurationFile(e.getMessage()); }

        System.out.println(strConfig);

        return strConfig;
    }

    private static final Set<String> defaultConfigs = Set.of("m","n","init_plan_min","init_plan_sec","init_budget",
            "init_center_dep","plan_rev_min","plan_rev_sec",
            "rev_cost","max_dep","interest_pct");
    private Map<String,Integer> compileConfig(String strConfig) throws IllegalConfiguration, MissingConfigurationVariable {
        Map<String,Integer> config = new HashMap<>();

        try {
            StatementParser configParser = new StatementParser(strConfig);
            configParser.execute(config);

        } catch (SyntaxError e) { throw new IllegalConfiguration(e.getMessage()); }

        Set<String> remainingKey = new HashSet<>(defaultConfigs);
        remainingKey.removeAll(config.keySet());

        if(!remainingKey.isEmpty()) {
            StringBuilder missingVariableSB = new StringBuilder();
            for(String var : remainingKey)
                missingVariableSB.append(var);

            throw new MissingConfigurationVariable("Configuration file is missing these variables : " + missingVariableSB);
        }

        return config;
    }


}
