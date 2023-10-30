package fuzs.configureddefaults;

import fuzs.configureddefaults.handler.DefaultFilesHandler;
import net.minecraftforge.forgespi.language.ILifecycleEvent;
import net.minecraftforge.forgespi.language.IModLanguageProvider;
import net.minecraftforge.forgespi.language.ModFileScanData;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfiguredDefaultsForge implements IModLanguageProvider {
    private static final Consumer<?> EMPTY_CONSUMER = $ -> {
        // NO-OP
    };

    public ConfiguredDefaultsForge() {
        DefaultFilesHandler.initialize();
    }

    @SuppressWarnings("unchecked")
    private static <T> Consumer<T> emptyConsumer() {
        return (Consumer<T>) EMPTY_CONSUMER;
    }

    @Override
    public String name() {
        return ConfiguredDefaults.MOD_NAME;
    }

    @Override
    public Consumer<ModFileScanData> getFileVisitor() {
        return emptyConsumer();
    }

    @Override
    public <R extends ILifecycleEvent<R>> void consumeLifecycleEvent(Supplier<R> consumeEvent) {
        // NO-OP
    }
}
