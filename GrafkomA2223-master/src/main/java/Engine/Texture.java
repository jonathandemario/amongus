package Engine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {
    private int textureId;
    private int width;
    private int height;

    public Texture(String filePath) throws Exception {
        // Create a new OpenGL texture
        textureId = GL11.glGenTextures();

        // Bind the texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        // Set texture parameters
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        // Load the image using STBImage
        ByteBuffer imageBuffer;
        int imageWidth;
        int imageHeight;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer channelsBuffer = stack.mallocInt(1);

            // Load the image file into a ByteBuffer
            imageBuffer = STBImage.stbi_load(filePath, widthBuffer, heightBuffer, channelsBuffer, 0);

            // Retrieve image width and height
            imageWidth = widthBuffer.get();
            imageHeight = heightBuffer.get();
        }

        if (imageBuffer == null) {
            throw new Exception("Failed to load texture image: " + filePath);
        }

        // Generate the texture
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, imageWidth, imageHeight, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);

        // Free the image buffer
        STBImage.stbi_image_free(imageBuffer);

        // Unbind the texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        // Set the width and height of the texture
        width = imageWidth;
        height = imageHeight;
    }

    public int getTextureId() {
        return textureId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
