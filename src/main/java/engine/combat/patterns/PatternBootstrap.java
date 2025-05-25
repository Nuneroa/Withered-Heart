package engine.combat.patterns;

import engine.combat.EnemyPattern;

public class PatternBootstrap {
    public static void registerDefaults() {
        //Make sure to implement a java class for each pattern
        // and register it here using the PatternRegistry.

        //PatternRegistry.register("zigzag", args -> {
        //    float speed = args.length > 0 ? args[0] : 120f;
        //    float amp   = args.length > 1 ? args[1] : 40f;
        //    return new ZigZagPattern(speed, amp);
        //});

        // Add more custom pattern registrations here
        // PatternRegistry.register("wave", args -> new WavePattern(...));

        
    }
}
