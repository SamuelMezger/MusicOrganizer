package extraction;

import java.awt.image.BufferedImage;

public class Crop {
    public static BufferedImage centerSquare(BufferedImage image) {

            int maxSideLength = Math.min(image.getHeight(), image.getWidth());
            int newWithStart = (image.getWidth() / 2) - (maxSideLength / 2);
            int newHeightStart = (image.getHeight() / 2) - (maxSideLength / 2);

            return image.getSubimage(newWithStart, newHeightStart, maxSideLength, maxSideLength);
    }
}
