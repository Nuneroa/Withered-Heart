package engine.graphics;

import static org.lwjgl.opengl.GL11.*;

import org.joml.Matrix4f;
import java.util.ArrayList;
import java.util.List;

public class Background {
    public static class Layer {
        public Texture texture;
        public float offsetY = 0f;
        public float speed = 30f;

        public Layer(Texture texture, float speed) {
            this.texture = texture;
            this.speed = speed;
        }

        public void update(float delta) {
            offsetY += speed * delta;
            if (offsetY >= texture.getHeight()) offsetY = 0;
        }

        public void render(Shader shader, SpriteRenderer renderer, Matrix4f projection) {
            renderer.draw(texture, shader, 0, offsetY, texture.getWidth(), texture.getHeight(), projection);
            renderer.draw(texture, shader, 0, offsetY - texture.getHeight(), texture.getWidth(), texture.getHeight(), projection);
        }
    }

    private List<Layer> layers = new ArrayList<>();

    public Background(Texture texture) {
        layers.add(new Layer(texture, 30f));
    }

    public void addLayer(Texture texture, float speed) {
        layers.add(new Layer(texture, speed));
    }

    public void update(float delta) {
        for (Layer layer : layers) {
            layer.update(delta);
        }
    }

    public void render(Shader shader, SpriteRenderer renderer, Matrix4f projection) {
        for (Layer layer : layers) {
            layer.render(shader, renderer, projection);
        }
    }

    public void setSpeed(float speed) {
        for (Layer layer : layers) {
            layer.speed = speed;
        }
    }

    public void setTexture(String path) {
        if (!layers.isEmpty()) {
            layers.get(0).texture = new Texture(path);
        }
    }

    public Texture getTexture() {
        return layers.isEmpty() ? null : layers.get(0).texture;
    }

    public void clearLayers() {
        layers.clear();
    }

    public void addScriptedLayer(String texturePath, float speed) {
        Texture tex = new Texture(texturePath);
        addLayer(tex, speed);
    }
}
