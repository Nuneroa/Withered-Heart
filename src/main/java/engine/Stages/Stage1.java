package engine.Stages;

import java.util.logging.Level;

import engine.script.LevelBuilder;
import engine.script.LevelScript;
import engine.script.ScriptContext;

public class Stage1 implements LevelScript {
    private final ScriptContext ctx;
    private final LevelBuilder builder;

    public Stage1(ScriptContext ctx) {
        this.ctx = ctx;
        this.builder = new LevelBuilder(ctx);
    }

    @Override
    public void onStart() {
      builder.setBackgroundSpeed(60);
    }

    @Override
    public void onUpdate(float delta) {
        if (!ctx.hasFlag("wave1")) {
            builder.spawnGroup("spirit", 5, 100, -40, 160, 90, "sling", "move_sine.txt");
            ctx.setFlag("wave1");
        }
        builder.waitUntilEnemiesCleared();
        builder.showMessage("Enemies Cleared!");
       
    }

    @Override
    public void onEnd() {
        for (float speed = 60f; speed > 0f; speed -= 1f) {
            builder.setBackgroundSpeed(speed);
            try {
            Thread.sleep(50); // Adjust the delay as needed
            } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            }
        }
        
        builder.fadeToBlack(5f);
    }
    

}
