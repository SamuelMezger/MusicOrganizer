package okhttp;

import extraction.Downloader;
import extraction.ExtractionException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class OkHttpDownloader implements Downloader {

    final OkHttpClient client = new OkHttpClient();

    @Override
    public String getOkString(String url) throws IOException, ExtractionException {
        try (Response response = this.getOkResponse(url)) {
            return response.body().string();
        }
    }

    @Override
    public BufferedImage getOkImage(String url) throws IOException, ExtractionException {
        try (Response response = this.getOkResponse(url)) {
            InputStream inputStream = response.body().byteStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            BufferedImage image = ImageIO.read(bufferedInputStream);
            response.close();

            if (image != null) {
                return image;
            } else {
                throw new ExtractionException("Could not decode Image " + url);
            }
        }
    }

    private Response getOkResponse(String url) throws IOException, ExtractionException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = this.client.newCall(request).execute();
        if (response.code() == 200) {
            return response;
        } else {
            throw new ExtractionException("Response code not 200 for " + url);
        }
    }
}
