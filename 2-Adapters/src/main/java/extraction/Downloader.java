package extraction;


import java.awt.image.BufferedImage;
import java.io.IOException;

public interface Downloader {
    String getOkString(String url) throws IOException, ExtractionException;

    BufferedImage getOkImage(String url) throws IOException, ExtractionException;
}
