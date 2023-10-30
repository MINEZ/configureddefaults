package fuzs.configureddefaults.core;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public Path getGameDirectory() {
        return FabricLoader.getInstance().getGameDir();
    }
}
