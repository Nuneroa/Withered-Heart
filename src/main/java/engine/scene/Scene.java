package engine.scene;

public abstract class Scene {
    public abstract void enter();
    public abstract void update(float delta);
    public abstract void render(float delta, long window);
    public abstract void exit();
}
