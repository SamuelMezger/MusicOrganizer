package extraction.youtube;

import extraction.ExtractionException;
import nanojson.JsonParserAdapter;
import org.junit.Assert;
import org.junit.Test;
import model.youtube.BasicVideoInfo;

public class NanoVideoInfoParserTest {

    @Test
    public void testFromJson() throws ExtractionException {
        String flatVideoInfoJson = "{\"_type\": \"url\", \"url\": \"OJdG8wsU8cw\", \"ie_key\": \"Youtube\", \"id\": \"OJdG8wsU8cw\", \"title\": \"TheFatRat & AleXa (\\uc54c\\ub809\\uc0ac) - Rule The World\"}";
        BasicVideoInfoParser basicVideoInfoParser = new NanoBasicVideoInfoParser(new JsonParserAdapter());
        BasicVideoInfo flatVideoInfo = basicVideoInfoParser.fromJson(flatVideoInfoJson);
        Assert.assertEquals("OJdG8wsU8cw", flatVideoInfo.getVideoId());
        Assert.assertEquals("TheFatRat & AleXa (알렉사) - Rule The World", flatVideoInfo.getVideoTitle());

    }

    @Test
    public void testFromJsonMissingIdFail() throws ExtractionException {
        String flatVideoInfoJson = "{\"_type\": \"url\", \"url\": \"OJdG8wsU8cw\", \"ie_key\": \"Youtube\", \"title\": \"TheFatRat & AleXa (\\uc54c\\ub809\\uc0ac) - Rule The World\"}";
        BasicVideoInfoParser basicVideoInfoParser = new NanoBasicVideoInfoParser(new JsonParserAdapter());
        BasicVideoInfo flatVideoInfo = basicVideoInfoParser.fromJson(flatVideoInfoJson);
    }
}