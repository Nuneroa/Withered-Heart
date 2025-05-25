package engine.script;
import engine.graphics.Background;
import engine.UI.HUD;
import engine.entity.EnemySpawner;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ScriptContext {
    private final Map<String, Boolean> flags;
    public final EnemySpawner spawner;
    public final Background background;
    public final HUD hud;

    public ScriptContext(EnemySpawner spawner, Background background, HUD hud) {
        this.spawner = spawner;
        this.background = background;
        this.hud = hud;
        this.flags = new HashMap<>();
    }

    public boolean hasFlag(String flag) {
        if (flag == null) {
            throw new IllegalArgumentException("Flag name cannot be null");
        }
        return flags.getOrDefault(flag, false);
    }

    public void setFlag(String flag) {
        if (flag == null) {
            throw new IllegalArgumentException("Flag name cannot be null");
        }
        flags.put(flag, true);
    }

    public void clearFlag(String flag) {
        if (flag == null) {
            throw new IllegalArgumentException("Flag name cannot be null");
        }
        flags.put(flag, false);
    }

    public void wait(float seconds) {
        try {
            Thread.sleep((long)(seconds * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Wait interrupted", e);
        }
    }

    public void waitUntil(String condition) {
        // Simple implementation that checks condition every 100ms
        while (!evaluateCondition(condition)) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Wait interrupted", e);
            }
        }
    }

    private boolean evaluateCondition(String condition) {
        // Basic condition evaluation - checks if a flag is true
        // Could be expanded to handle more complex conditions
        return hasFlag(condition);
    }
    public void waitUntilEnemiesDefeated() {
    while (spawner.getEnemies().stream().anyMatch(e -> e.isActive())) {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Wait interrupted", e);
        }
    }
}
}
