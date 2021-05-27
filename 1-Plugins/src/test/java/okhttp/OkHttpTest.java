package okhttp;

import extraction.ExtractionException;
import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class OkHttpTest {
    final OkHttpDownloader dl = new OkHttpDownloader();

    @Test
    public void testGetImageJpeg() throws IOException, ExtractionException {
        String url = "https://is2-ssl.mzstatic.com/image/thumb/Music124/v4/02/aa/ec/02aaece2-669f-3ea8-ffdb-cbe845381fe4/source/30x30bb.jpg";
        BufferedImage image = this.dl.getOkImage(url);
        Assert.assertNotNull(image);
        Assert.assertEquals(30, image.getWidth());
    }

    @Test
    public void testGetImageWebP() throws IOException, ExtractionException {
        String url = "https://i.ytimg.com/vi_webp/OJdG8wsU8cw/mqdefault.webp";
        BufferedImage image = this.dl.getOkImage(url);
        Assert.assertNotNull(image);
        Assert.assertEquals(320, image.getWidth());
    }

    @Test
    public void testGetImageNotAvailable() throws IOException {
        String url = "https://i.ytimg.com/vi_webp/OJdG8wsU8cw/notavailable.webp";

        try {
            BufferedImage image = this.dl.getOkImage(url);
            Assert.fail();
        } catch (ExtractionException e) {
            Assert.assertTrue(e.getMessage().startsWith("Response code not 200 for"));
        }
    }

    @Test
    public void testGetImageNotAnImage() throws IOException {
        String url = "https://www.example.com";

        try {
            BufferedImage image = this.dl.getOkImage(url);
            Assert.fail();
        } catch (ExtractionException e) {
            Assert.assertTrue(e.getMessage().startsWith("Could not decode Image"));
        }
    }

    @Test
    public void testGetString() throws IOException, ExtractionException {
        String url = "https://www.example.com";

        String result = this.dl.getOkString(url);
        Assert.assertTrue(result.contains("Example Domain"));
    }
}
