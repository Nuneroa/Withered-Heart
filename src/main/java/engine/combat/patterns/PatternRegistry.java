package engine.combat.patterns;

import engine.combat.EnemyPattern;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * PatternRegistry is a central registry for bullet patterns (EnemyPattern) used by enemies.
 * It allows registering new patterns by name and retrieving them with arguments.
 * This enables dynamic and extensible bullet pattern scripting, e.g. from a DSL or stage script.
 */
public class PatternRegistry {
    // Maps pattern names (lowercase) to factory functions that create EnemyPattern instances.
    private static final Map<String, Function<float[], EnemyPattern>> registry = new HashMap<>();

    static {
        // Register the default "radial" pattern.
        // Usage: getPattern("radial", count, speed)
        register("radial", args -> {
            int count = (int) (args.length > 0 ? args[0] : 10); // default 10 bullets
            float speed = args.length > 1 ? args[1] : 100f;      // default speed 100
            return new RadialPattern(count, speed);
        });

        // Register the default "spiral" pattern.
        // Usage: getPattern("spiral", bullets, spiralSpeed)
        register("spiral", args -> {
            int bullets = (int) (args.length > 0 ? args[0] : 12); // default 12 bullets
            float spiralSpeed = args.length > 1 ? args[1] : 100f;  // default speed 100
            return new SpiralPattern(bullets, spiralSpeed);
        });

        // Register the default "directional" pattern.
        // Usage: getPattern("directional", angle, speed)
        register("directional", args -> {
            float angle = args.length > 0 ? args[0] : 90f;        // default angle 90 degrees
            float dirSpeed = args.length > 1 ? args[1] : 150f;    // default speed 150
            return new SimpleDirectionalPattern(angle, dirSpeed);
        });
        register("Sling", args -> {
            float speed = args.length > 0 ? args[0] : 400f; // default speed 120
            return new SlingShot(speed);
        });
        
    }

    
    /**
     * Registers a new pattern factory under the given name.
     * @param name The pattern name (case-insensitive).
     * @param factory A function that takes float[] arguments and returns an EnemyPattern.
     */
    public static void register(String name, Function<float[], EnemyPattern> factory) {
        registry.put(name.toLowerCase(), factory);
    }

    /**
     * Retrieves and constructs a pattern by name and arguments.
     * @param name The pattern name (case-insensitive).
     * @param args Arguments to pass to the pattern factory.
     * @return An EnemyPattern instance.
     * @throws IllegalArgumentException if the pattern is not registered.
     */
    public static EnemyPattern getPattern(String name, float... args) {
        Function<float[], EnemyPattern> factory = registry.get(name.toLowerCase());
        if (factory == null) throw new IllegalArgumentException("Pattern not registered: " + name);
        return factory.apply(args);
    }

    /**
     * Checks if a pattern with the given name is registered.
     * @param name The pattern name (case-insensitive).
     * @return true if registered, false otherwise.
     */
    public static boolean isRegistered(String name) {
        return registry.containsKey(name.toLowerCase());
    }
}