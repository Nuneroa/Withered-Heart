package engine.graphics;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class SpriteRenderer {
    private int vao, vbo;
    private static final FloatBuffer MATRIX_BUFFER = BufferUtils.createFloatBuffer(16);

    private final float[] vertices = {
        // pos      // tex
        0f, 1f,     0f, 1f,
        1f, 0f,     1f, 0f,
        0f, 0f,     0f, 0f,

        0f, 1f,     0f, 1f,
        1f, 1f,     1f, 1f,
        1f, 0f,     1f, 0f
    };

    public SpriteRenderer() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void draw(Texture texture, Shader shader, float x, float y, float width, float height, Matrix4f projection) {
    Matrix4f model = new Matrix4f().translate(x, y, 0).scale(width, height, 1);
    shader.bind();

    int projLoc = glGetUniformLocation(shader.getProgramId(), "projection");
    int modelLoc = glGetUniformLocation(shader.getProgramId(), "model");

    MATRIX_BUFFER.clear();
    projection.get(MATRIX_BUFFER);
    glUniformMatrix4fv(projLoc, false, MATRIX_BUFFER);

    MATRIX_BUFFER.clear();
    model.get(MATRIX_BUFFER);
    glUniformMatrix4fv(modelLoc, false, MATRIX_BUFFER);

    glBindVertexArray(vao);
    texture.bind();
    glDrawArrays(GL_TRIANGLES, 0, 6);
    glBindVertexArray(0);
    shader.unbind();
}

}
