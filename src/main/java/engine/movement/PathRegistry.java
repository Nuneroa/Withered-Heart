package engine.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PathRegistry {
    private static final Map<String, Function<float[], MovementPath>> registry = new HashMap<>();

    static {
        register("linear", args -> new LinearPath(args[0], args[1]));
        register("sine", args -> new SineWavePath(args[0], args[1]));
        // Additional paths can be registered here
    }

    public static void register(String name, Function<float[], MovementPath> factory) {
        registry.put(name.toLowerCase(), factory);
    }

    public static MovementPath get(String name, float... args) {
        Function<float[], MovementPath> factory = registry.get(name.toLowerCase());
        if (factory == null) throw new IllegalArgumentException("Unknown path: " + name);
        return factory.apply(args);
    }
}
