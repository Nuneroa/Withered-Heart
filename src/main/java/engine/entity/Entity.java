package engine.entity;

import engine.graphics.Shader;
import engine.graphics.SpriteRenderer;
import engine.graphics.Texture;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Matrix4f;

public abstract class Entity {
    protected float x, y;
    protected Texture texture;

    public Entity(float x, float y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
    }

    public abstract void update(float delta);

    public abstract void render(Shader shader, SpriteRenderer renderer, Matrix4f projection);

public float getWidth() {
    return texture.getWidth();
}

public float getHeight() {
    return texture.getHeight();
}

// Add to Enemy.java
public float getScaledWidth() {
    return texture.getWidth() / 6f;
}

public float getScaledHeight() {
    return texture.getHeight() / 6f;
}


public float getCenterX() {
    return x + getScaledWidth() / 2f;
}


public float getCenterY() {
    return y + getScaledHeight() / 2f;
}
}
