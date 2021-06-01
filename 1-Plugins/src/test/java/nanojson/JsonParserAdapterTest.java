package nanojson;

import extraction.ExtractionException;
import extraction.json.JsonParserI;
import org.junit.Assert;
import org.junit.Test;

public class JsonParserAdapterTest {
    final private String itunesResponseBody =
            "\n" +
            "\n" +
            "\n" +
            "{\n" +
            " \"resultCount\":2,\n" +
            " \"results\": [\n" +
            "{\"wrapperType\":\"track\", \"kind\":\"song\", \"artistId\":1214657704, \"collectionId\":1527801023, \"trackId\":1527801024, \"artistName\":\"Nattalia Sarria\", \"collectionName\":\"Plastic Love (From \\\"Mariya Takeuchi\\\") - Single\", \"trackName\":\"Plastic Love (From \\\"Mariya Takeuchi\\\")\", \"collectionCensoredName\":\"Plastic Love (From \\\"Mariya Takeuchi\\\") - Single\", \"trackCensoredName\":\"Plastic Love (From \\\"Mariya Takeuchi\\\")\", \"artistViewUrl\":\"https://music.apple.com/us/artist/nattalia-sarria/1214657704?uo=4\", \"collectionViewUrl\":\"https://music.apple.com/us/album/plastic-love-from-mariya-takeuchi/1527801023?i=1527801024&uo=4\", \"trackViewUrl\":\"https://music.apple.com/us/album/plastic-love-from-mariya-takeuchi/1527801023?i=1527801024&uo=4\", \n" +
            "\"previewUrl\":\"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview124/v4/c2/0b/41/c20b4115-8840-2c22-01a8-e95475f9d0ac/mzaf_9278249742700774678.plus.aac.p.m4a\", \"artworkUrl30\":\"https://is5-ssl.mzstatic.com/image/thumb/Music114/v4/a7/dd/b1/a7ddb108-7f89-13a2-8688-1046740923c4/source/30x30bb.jpg\", \"artworkUrl60\":\"https://is5-ssl.mzstatic.com/image/thumb/Music114/v4/a7/dd/b1/a7ddb108-7f89-13a2-8688-1046740923c4/source/60x60bb.jpg\", \"artworkUrl100\":\"https://is5-ssl.mzstatic.com/image/thumb/Music114/v4/a7/dd/b1/a7ddb108-7f89-13a2-8688-1046740923c4/source/100x100bb.jpg\", \"collectionPrice\":0.99, \"trackPrice\":0.99, \"releaseDate\":\"2020-08-14T12:00:00Z\", \"collectionExplicitness\":\"notExplicit\", \"trackExplicitness\":\"notExplicit\", \"discCount\":1, \"discNumber\":1, \"trackCount\":1, \"trackNumber\":1, \"trackTimeMillis\":279297, \"country\":\"USA\", \"currency\":\"USD\", \"primaryGenreName\":\"Pop\", \"isStreamable\":true}, \n" +
            "{\"wrapperType\":\"track\", \"kind\":\"song\", \"artistId\":41754449, \"collectionId\":1541673202, \"trackId\":1541673399, \"artistName\":\"Mariya Takeuchi\", \"collectionName\":\"Expressions (MOON Version)\", \"trackName\":\"Plastic Love\", \"collectionCensoredName\":\"Expressions (MOON Version)\", \"trackCensoredName\":\"Plastic Love\", \"artistViewUrl\":\"https://music.apple.com/us/artist/mariya-takeuchi/41754449?uo=4\", \"collectionViewUrl\":\"https://music.apple.com/us/album/plastic-love/1541673202?i=1541673399&uo=4\", \"trackViewUrl\":\"https://music.apple.com/us/album/plastic-love/1541673202?i=1541673399&uo=4\", \n" +
            "\"previewUrl\":\"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview124/v4/54/6e/78/546e78b8-eed1-aae7-7d94-9ac0e6f5fe19/mzaf_1756424326571745482.plus.aac.p.m4a\", \"artworkUrl30\":\"https://is5-ssl.mzstatic.com/image/thumb/Music124/v4/ef/23/57/ef2357c0-7b42-bf5b-bf7e-699037d27c73/source/30x30bb.jpg\", \"artworkUrl60\":\"https://is5-ssl.mzstatic.com/image/thumb/Music124/v4/ef/23/57/ef2357c0-7b42-bf5b-bf7e-699037d27c73/source/60x60bb.jpg\", \"artworkUrl100\":\"https://is5-ssl.mzstatic.com/image/thumb/Music124/v4/ef/23/57/ef2357c0-7b42-bf5b-bf7e-699037d27c73/source/100x100bb.jpg\", \"releaseDate\":\"1984-04-25T08:00:00Z\", \"collectionExplicitness\":\"notExplicit\", \"trackExplicitness\":\"notExplicit\", \"discCount\":1, \"discNumber\":1, \"trackCount\":32, \"trackNumber\":9, \"trackTimeMillis\":294493, \"country\":\"USA\", \"currency\":\"USD\", \"primaryGenreName\":\"J-Pop\", \"isStreamable\":true}]\n" +
            "}\n" +
            "\n" +
            "\n";

    @Test
    public void jsonObjectFrom() throws ExtractionException {
        JsonParserI.JsonObjectI jsonResult = new JsonParserAdapter().jsonObjectFrom(this.itunesResponseBody).getArray("results").getObject(1);
        String artist = jsonResult.getString("artistName");
        Assert.assertEquals("Mariya Takeuchi", artist);
        String trackName = jsonResult.getString("trackName");
        Assert.assertEquals("Plastic Love", trackName);
        int trackNr = jsonResult.getInt("trackNumber");
        Assert.assertEquals(9, trackNr);
    }
    @Test
    public void forEachTest() throws ExtractionException {
        JsonParserI.JsonArrayI jsonResult = new JsonParserAdapter().jsonObjectFrom(this.itunesResponseBody).getArray("results");
//        for (Object jsonObject : jsonResult) {
//            System.out.println("yay");
//        }

    }

}