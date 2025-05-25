package engine.graphics;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTAlignedQuad;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class TextRenderer {
    private static final int BITMAP_W = 512;
    private static final int BITMAP_H = 512;

    private int texId;
    private STBTTBakedChar.Buffer charData;
    private Shader shader;
    private int vao, vbo;

    public TextRenderer(String fontPath, int fontSize) {
        try {
            shader = new Shader("shaders/text.vert", "shaders/text.frag");
            ByteBuffer ttf;
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(fontPath)) {
                if (is == null) throw new RuntimeException("Font not found: " + fontPath);
                byte[] data = is.readAllBytes();
                ttf = BufferUtils.createByteBuffer(data.length);
                ttf.put(data);
                ttf.flip();
            }

            ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);
            charData = STBTTBakedChar.malloc(96);
            stbtt_BakeFontBitmap(ttf, fontSize, bitmap, BITMAP_W, BITMAP_H, 32, charData);

            texId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texId);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, BITMAP_W, BITMAP_H, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

            vao = glGenVertexArrays();
            vbo = glGenBuffers();

            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, 6 * 4 * Float.BYTES, GL_DYNAMIC_DRAW);

            glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
            glEnableVertexAttribArray(1);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize TextRenderer", e);
        }
    }

    public void renderText(String text, float x, float y, float scale, Matrix4f projection) {
        renderText(text, x, y, scale, projection, 1f);
    }

    public void renderText(String text, float x, float y, float scale, Matrix4f projection, float alpha) {
        shader.bind();

        int projLoc = glGetUniformLocation(shader.getProgramId(), "projection");
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        projection.get(fb);
        glUniformMatrix4fv(projLoc, false, fb);

        int colorLoc = glGetUniformLocation(shader.getProgramId(), "textColor");
        glUniform4f(colorLoc, 1f, 1f, 1f, alpha);

        int texLoc = glGetUniformLocation(shader.getProgramId(), "text");
        glUniform1i(texLoc, 0); // Bind sampler2D to texture unit 0

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texId);
        glBindVertexArray(vao);

        float startX = x;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 32 || c >= 128) continue;

            STBTTAlignedQuad q = STBTTAlignedQuad.malloc();
            float[] xpos = {x};
            float[] ypos = {y};
            stbtt_GetBakedQuad(charData, BITMAP_W, BITMAP_H, c - 32, xpos, ypos, q, true);

            float[] vertices = {
                q.x0(), q.y0(), q.s0(), q.t0(),
                q.x1(), q.y0(), q.s1(), q.t0(),
                q.x0(), q.y1(), q.s0(), q.t1(),

                q.x0(), q.y1(), q.s0(), q.t1(),
                q.x1(), q.y0(), q.s1(), q.t0(),
                q.x1(), q.y1(), q.s1(), q.t1(),
            };

            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
            glDrawArrays(GL_TRIANGLES, 0, 6);

            q.free();
            x = xpos[0];
        }

        glBindVertexArray(0);
        shader.unbind();
    }

    
}
