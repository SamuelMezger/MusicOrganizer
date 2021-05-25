package sapher;

import org.junit.Assert;
import org.junit.Test;
import model.youtube.BasicVideoInfo;

import java.util.Optional;

public class JacksonVideoInfoParserTest {

    @Test
    public void testFromJson() {
        String flatVideoInfoJson = "{\"_type\": \"url\", \"url\": \"OJdG8wsU8cw\", \"ie_key\": \"Youtube\", \"id\": \"OJdG8wsU8cw\", \"title\": \"TheFatRat & AleXa (\\uc54c\\ub809\\uc0ac) - Rule The World\"}";
        BasicVideoInfoParser basicVideoInfoParser = new JacksonVideoInfoParser();
        Optional<BasicVideoInfo> flatVideoInfoOpt = basicVideoInfoParser.fromJson(flatVideoInfoJson);
        flatVideoInfoOpt.ifPresentOrElse(flatVideoInfo -> {
            Assert.assertEquals("OJdG8wsU8cw", flatVideoInfo.getVideoId());
            Assert.assertEquals("TheFatRat & AleXa (알렉사) - Rule The World", flatVideoInfo.getVideoTitle());
        }, () -> Assert.fail("Parsing of FlatVideoInfoJson failed"));
    }
    
    @Test
    public void testFromJsonMissingIdFail() {
        String flatVideoInfoJson = "{\"_type\": \"url\", \"url\": \"OJdG8wsU8cw\", \"ie_key\": \"Youtube\", \"title\": \"TheFatRat & AleXa (\\uc54c\\ub809\\uc0ac) - Rule The World\"}";
        BasicVideoInfoParser basicVideoInfoParser = new JacksonVideoInfoParser();
        Optional<BasicVideoInfo> flatVideoInfoOpt = basicVideoInfoParser.fromJson(flatVideoInfoJson);
        flatVideoInfoOpt.ifPresent(System.out::println);
        Assert.assertFalse(flatVideoInfoOpt.isPresent());
    }
}