package engine.script;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import engine.movement.LinearPath;
import engine.movement.MovementPath;
import engine.movement.SineWavePath;
import engine.movement.WaypointPath;

public class MovementScriptLoader {

    public static MovementPath load(String scriptFile) {
        String fullPath = scriptFile;

        if (!fullPath.endsWith(".txt")) {
            fullPath += ".txt"; // Ensure it has the correct extension
        }

        try (InputStream is = MovementScriptLoader.class.getClassLoader().getResourceAsStream(fullPath)) {
            if (is == null) {
                System.err.println("[MovementScriptLoader] ❌ Script not found: " + fullPath);
                return new LinearPath(0f, 60f); // Safe fallback
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            Map<String, String> config = new HashMap<>();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("=")) {
                    String[] parts = line.split("=");
                    config.put(parts[0].trim().toLowerCase(), parts[1].trim());
                }
            }

            String type = config.getOrDefault("type", "linear");

            switch (type.toLowerCase()) {
                case "linear":
                    float vx = Float.parseFloat(config.getOrDefault("vx", "0"));
                    float vy = Float.parseFloat(config.getOrDefault("vy", "60"));
                    return new LinearPath(vx, vy);

                case "sine":
                    float amp = Float.parseFloat(config.getOrDefault("amplitude", "10"));
                    float freq = Float.parseFloat(config.getOrDefault("frequency", "2"));
                    return new SineWavePath(amp, freq);

                case "waypoint":
                    float speed = Float.parseFloat(config.getOrDefault("speed", "50"));
                    WaypointPath wp = new WaypointPath(speed);

                    for (int i = 0; ; i++) {
                        String key = "point" + i;
                        if (!config.containsKey(key)) break;
                        String[] coords = config.get(key).split(",");
                        if (coords.length == 2) {
                            wp.addWaypoint(
                                Float.parseFloat(coords[0].trim()),
                                Float.parseFloat(coords[1].trim())
                            );
                        }
                    }

                    String easingName = config.getOrDefault("easing", "linear");
                    wp.setEasing(engine.movement.Easing.get(easingName));
                    return wp;

                default:
                    System.err.println("[MovementScriptLoader] ❌ Unknown movement type: " + type);
                    return new LinearPath(0f, 60f); // fallback
            }

        } catch (Exception e) {
            System.err.println("[MovementScriptLoader] ⚠️ Failed to load script: " + fullPath + " - " + e.getMessage());
            e.printStackTrace();
            return new LinearPath(0f, 60f); // fallback on error
        }
    }
}
