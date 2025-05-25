package engine.script;

public interface LevelScript {
    void onStart();
    void onUpdate(float delta);
    void onEnd();
}
