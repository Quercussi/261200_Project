import java.util.Map;

public class Upbeat {

    public static void main(String[] args) throws MissingConfigurationFile, MissingConfigurationVariable, IllegalConfiguration {
        ConfigurationReader configurationReader = new ConfigurationReader("config.txt");

        Map<String,Integer> config = configurationReader.getConfig();

        for(String key : config.keySet()) {
            System.out.println(key);
        }
    }
}
