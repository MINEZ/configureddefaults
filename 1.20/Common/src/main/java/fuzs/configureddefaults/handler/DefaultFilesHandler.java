package fuzs.configureddefaults.handler;

import fuzs.configureddefaults.ConfiguredDefaults;
import fuzs.configureddefaults.core.CommonAbstractions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class DefaultFilesHandler {
    private static final Path GAME_PATH = CommonAbstractions.INSTANCE.getGameDirectory();
    private static final Path GAME_PARENT_PATH = GAME_PATH.getParent();
    private static final Path DEFAULT_PRESETS_PATH = GAME_PATH.resolve(ConfiguredDefaults.MOD_ID);
    private static final Path README_PATH = DEFAULT_PRESETS_PATH.resolve("README.md");
    private static final Set<Path> BLACKLISTED_PATHS = Set.of(DEFAULT_PRESETS_PATH, README_PATH);
    private static final String README_FILE = """
            # %1$s
            
            This whole directory servers as a synchronized mirror of `.minecraft`. Every sub-directory and / or file placed within will be copied to the main `.minecraft` directory during game launch if the directory / file is not already present.
            There is no way of overriding an existing file, a copy will only be made when the target destination is empty.
            
            Please note that due to the way Minecraft handles `options.txt` specifically it is sufficient to include only the options you want to set a preset for. All missing options will be filled in using their internal defaults when the file is read by the game.
            
            Examples:
            - `.minecraft/%2$s/options.txt` will be copied to `.minecraft/options.txt` if not already present
            - `.minecraft/%2$s/config/jei/jei.toml` will be copied to `.minecraft/config/jei/jei.toml` if not already present
            
            Note that this `README.md` file is excluded from being copied to `.minecraft`.
            """.formatted(ConfiguredDefaults.MOD_NAME, ConfiguredDefaults.MOD_ID);

    private static boolean initialized;

    public static void initialize() {
        // this is constructed twice, we only need to run once
        // (once when Forge is counting available language loaders, and the second time when the language loader is actually used)
        if (!initialized) {
            initialized = true;
            ConfiguredDefaults.LOGGER.info("Applying default files...");
            try {
                trySetupFresh();
                tryCopyFiles();
            } catch (IOException exception) {
                ConfiguredDefaults.LOGGER.error("Failed to setup default files", exception);
            }
        }
    }

    private static void trySetupFresh() throws IOException {
        if (Files.notExists(DEFAULT_PRESETS_PATH)) {
            if (!DEFAULT_PRESETS_PATH.toFile().mkdir()) {
                ConfiguredDefaults.LOGGER.info("Failed to create fresh '{}' directory", relativizeAndNormalize(DEFAULT_PRESETS_PATH));
                return;
            } else {
                ConfiguredDefaults.LOGGER.info("Successfully created fresh '{}' directory", relativizeAndNormalize(DEFAULT_PRESETS_PATH));
            }
        }
        if (Files.notExists(README_PATH)) {
            Files.write(README_PATH, README_FILE.getBytes());
            ConfiguredDefaults.LOGGER.info("Successfully created fresh '{}' file", relativizeAndNormalize(README_PATH));
        }
    }

    private static void tryCopyFiles() throws IOException {
        Files.walk(DEFAULT_PRESETS_PATH).forEach(sourcePath -> {
            if (!BLACKLISTED_PATHS.contains(sourcePath)) {
                Path targetPath = GAME_PATH.resolve(DEFAULT_PRESETS_PATH.relativize(sourcePath)).normalize();
                // check if file already exists, otherwise copy will throw an exception
                if (sourcePath.toFile().exists() && !targetPath.toFile().exists()) {
                    try {
                        // we do not need to handle creating parent directories as the file tree is traversed depth-first
                        Files.copy(sourcePath, targetPath);
                        ConfiguredDefaults.LOGGER.info("Successfully copied '{}' to '{}'", relativizeAndNormalize(sourcePath), relativizeAndNormalize(targetPath));
                    } catch (IOException e) {
                        ConfiguredDefaults.LOGGER.info("Failed to copy '{}' to '{}'", relativizeAndNormalize(sourcePath), relativizeAndNormalize(targetPath));
                    }
                }
            }
        });
    }

    private static Path relativizeAndNormalize(Path path) {
        return GAME_PARENT_PATH.relativize(path).normalize();
    }
}
