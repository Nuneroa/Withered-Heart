package engine.Stages;

import engine.script.LevelScript;
import engine.script.LevelBuilder;
import engine.script.ScriptContext;

public class ExampleStage implements LevelScript {
    private final ScriptContext ctx;
    private final LevelBuilder builder;

    public ExampleStage(ScriptContext ctx) {
        this.ctx = ctx;
        this.builder = new LevelBuilder(ctx);
    }

    @Override
    public void onStart() {
        builder.setBackgroundSpeed(60);
        builder.showMessage("Stage 1 Begins!");
    }

    @Override
    public void onUpdate(float delta) {
        if (!ctx.hasFlag("wave1")) {
            builder.spawnGroup("spirit", 5, 100, -40, 60, 90, "sling", "move_zigzag.txt");
            ctx.setFlag("wave1");
        }

        if (ctx.hasFlag("wave1") && !ctx.hasFlag("cleared")) {
            builder.waitUntilEnemiesCleared();
            builder.showMessage("Enemies Cleared!");
            ctx.setFlag("cleared");
            builder.endStage();
        }
    }

    @Override
    public void onEnd() {
        builder.setBackgroundSpeed(0f);
        builder.fadeToBlack(5f);
    }
}
