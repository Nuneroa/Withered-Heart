package engine.renderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private final int id;
    private final int width, height;

    // In Texture.java
    // Add to Texture.java

    public Texture(int width, int height) {
        this.width = width;
        this.height = height;
        this.id = glGenTextures();
        
        glBindTexture(GL_TEXTURE_2D, id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 
                    0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glBindTexture(GL_TEXTURE_2D, 0);
    }


    public Texture(String filePath) {
        System.out.println("[Texture] Attempting to load: " + filePath);
    
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
    
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (stream == null) {
                System.err.println("[Texture] Resource not found: " + filePath);
            }
            ByteBuffer imageBuffer = ioResourceToByteBuffer(stream, 8192);
            ByteBuffer image = stbi_load_from_memory(imageBuffer, width, height, channels, 0);
            if (image == null) {
                System.err.println("[Texture] STBImage failed to load: " + filePath + " Reason: " + stbi_failure_reason());
                throw new RuntimeException("Failed to load texture: " + stbi_failure_reason());
            }
            this.width = width.get(0);
            this.height = height.get(0);
            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            int format = channels.get(0) == 4 ? GL_RGBA : GL_RGB;
            glTexImage2D(GL_TEXTURE_2D, 0, format, this.width, this.height, 0, 
                        format, GL_UNSIGNED_BYTE, image);
            stbi_image_free(image);
            System.out.println("[Texture] Loaded: " + filePath + " (" + this.width + "x" + this.height + ") id=" + id);
        } catch (IOException e) {
            System.err.println("[Texture] IOException: " + filePath);
            throw new RuntimeException("Failed to load texture: " + filePath, e);
        }
    }

    private ByteBuffer ioResourceToByteBuffer(InputStream stream, int bufferSize) throws IOException {
        ReadableByteChannel rbc = Channels.newChannel(stream);
        ByteBuffer buffer = BufferUtils.createByteBuffer(bufferSize);
        while (true) {
            int bytes = rbc.read(buffer);
            if (bytes == -1) break;
            if (buffer.remaining() == 0)
                buffer = resizeBuffer(buffer, buffer.capacity() * 2);
        }
        buffer.flip();
        return buffer;
    }

    private ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    public int getID() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }    public void bind(int textureUnit) {
        glActiveTexture(GL_TEXTURE0 + textureUnit);
        glBindTexture(GL_TEXTURE_2D, id);
        System.out.println("[Texture] Binding texture " + id + " to unit " + textureUnit + " (size: " + width + "x" + height + ")");
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    public int getId() { return id; }

    public void destroy() {
        glDeleteTextures(id);
    }

    public String toString() {
        return "Texture[id=" + id + ", size=" + width + "x" + height + "]";
    }
}
