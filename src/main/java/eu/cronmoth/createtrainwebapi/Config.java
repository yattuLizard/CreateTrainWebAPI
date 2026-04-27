package eu.cronmoth.createtrainwebapi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

public class Config {
    public static int SERVER_PORT = 8080;
    public static String SERVER_HOST = "0.0.0.0";
    public static String TRAIN_MODEL_PATH = "bluemap/train_models/";

    private static final String FILE_NAME = "createtrainwebapi.toml";

    public static void load(Path configDir) {
        try {
            if (!Files.exists(configDir)) Files.createDirectories(configDir);
            Path file = configDir.resolve(FILE_NAME);
            if (!Files.exists(file)) {
                String defaultToml = "# Create Train Web API config\n" +
                        "SERVER_PORT = 8080\n" +
                        "SERVER_HOST = \"0.0.0.0\"\n" +
                        "TRAIN_MODEL_PATH = \"bluemap/train_models/\"\n";
                Files.write(file, defaultToml.getBytes(), StandardOpenOption.CREATE_NEW);
            }

            String content = Files.readString(file);
            TomlParseResult result = Toml.parse(content);

            if (result.contains("SERVER_PORT")) {
                SERVER_PORT = result.getLong("SERVER_PORT").intValue();
            }

            if (result.contains("SERVER_HOST")) {
                SERVER_HOST = result.getString("SERVER_HOST");
            }

            if (result.contains("TRAIN_MODEL_PATH")) {
                TRAIN_MODEL_PATH = result.getString("TRAIN_MODEL_PATH");
            }

        } catch (IOException e) {
            // ignore and keep defaults
            e.printStackTrace();
        }
    }
}
