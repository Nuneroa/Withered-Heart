package engine.util;

public class Position {
    public static float centerX(float spriteWidth, float screenWidth) {
        return (screenWidth - spriteWidth) / 2f;
    }

    public static float centerY(float spriteHeight, float screenHeight) {
        return (screenHeight - spriteHeight) / 2f;
    }

    public static float alignLeft(float margin) {
        return margin;
    }

    public static float alignRight(float spriteWidth, float screenWidth, float margin) {
        return screenWidth - spriteWidth - margin;
    }

    public static float top(float margin) {
        return margin;
    }

    public static float bottom(float spriteHeight, float screenHeight, float margin) {
        return screenHeight - spriteHeight - margin;
    }
}
