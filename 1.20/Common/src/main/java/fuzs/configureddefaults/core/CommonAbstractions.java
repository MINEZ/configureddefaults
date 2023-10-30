package fuzs.configureddefaults.core;

import java.nio.file.Path;

public interface CommonAbstractions {
    CommonAbstractions INSTANCE = ServiceProviderHelper.load(CommonAbstractions.class);

    Path getGameDirectory();
}
