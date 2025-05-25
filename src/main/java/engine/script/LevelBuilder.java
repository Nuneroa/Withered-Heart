package engine.script;

import engine.combat.EnemyPattern;
import engine.entity.EnemySpawner;
import engine.graphics.Background;
import engine.UI.HUD;
import engine.movement.PathRegistry;
import engine.script.MovementScriptLoader;

public class LevelBuilder {
    private final ScriptContext ctx;

    public LevelBuilder(ScriptContext ctx) {
        this.ctx = ctx;
    }

    public void spawnWave(String type, float y, float spacing, int count, float angle) {
        float startX = 200;
        for (int i = 0; i < count; i++) {
            ctx.spawner.spawn(type, startX + spacing * i, y, angle);
        }
    }

    public void spawnAt(String type, float x, float y, String pattern, String moveScript) {
    ctx.spawner.spawnWithPatternAndPath(type, x, y, 90, pattern, MovementScriptLoader.load("scripts/" + moveScript));
    }


    public void spawnWaveWithPatternAndPath(
    String type, float y, float spacing, int count, float angle,
    String patternType, String pathType,
    float[] patternArgs, float[] pathArgs) {
    
    for (int i = 0; i < count; i++) {
        float x = 200 + spacing * i;
        ctx.spawner.spawnWithPatternAndPath( type, x, y, angle, patternType, PathRegistry.get(pathType, pathArgs)
        );
    }
}
    public void spawnWaveWithMovementScript(
    String type, float y, float spacing, int count, float angle,
    String patternType, float[] patternArgs, String movementScriptFile) {

    for (int i = 0; i < count; i++) {
        float x = 200 + spacing * i;
        ctx.spawner.spawnWithPatternAndPath(
            type, x, y, angle,
            patternType,
            MovementScriptLoader.load("scripts/" + movementScriptFile)
        );
    }
}
    public void spawnGrid(String type, int cols, int rows, float startX, float startY, float spacingX, float spacingY, String pattern, String moveScript) {
    for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
            float x = startX + col * spacingX;
            float y = startY + row * spacingY;
            spawnAt(type, x, y, pattern, moveScript);
        }
    }
}
    public void spawnCircle(String type, float centerX, float centerY, int count, float radius, String pattern, String moveScript) {
        for (int i = 0; i < count; i++) {
            float angle = (float) (i * 2 * Math.PI / count);
            float x = centerX + radius * (float) Math.cos(angle);
            float y = centerY + radius * (float) Math.sin(angle);
            spawnAt(type, x, y, pattern, moveScript);
        }
    }

    

    public void setBackgroundSpeed(float speed) {
        ctx.background.setSpeed(speed);
    }

    public void setBackgroundTexture(String texturePath) {
        ctx.background.setTexture(texturePath);
    }

    public void showMessage(String text) {
        System.out.println("[SCRIPT MSG] " + text);
        // Could integrate HUD or screen text in the future
    }

    public void fadeToBlack(float duration) {
        // Placeholder: implement fade effect in scene system
        System.out.println("Fading to black over " + duration + "s");
    }
    public void endStage() {
        System.out.println("Ending stage...");
        
        // Placeholder: implement stage transition logic
        
    }

    public void spawnGroup(String type, int count, float xStart, float y, float spacing, float angle, String pattern, String moveScript) {
    for (int i = 0; i < count; i++) {
        float x = xStart + i * spacing;
        ctx.spawner.spawnWithPatternAndPath(
            type, x, y, angle,
            pattern,
            MovementScriptLoader.load("scripts/" + moveScript)
        );
    }
}

    public void wait(float seconds) {
        ctx.wait(seconds);
    }

    public void waitUntilEnemiesCleared() {
        ctx.waitUntilEnemiesDefeated();
    }

    public void waitUntil(String flag) {
        ctx.waitUntil(flag);
    }
}