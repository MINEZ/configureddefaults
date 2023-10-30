package fuzs.configureddefaults.core;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ForgeAbstractions implements CommonAbstractions {

    @Override
    public Path getGameDirectory() {
        return FMLPaths.GAMEDIR.get();
    }
}
