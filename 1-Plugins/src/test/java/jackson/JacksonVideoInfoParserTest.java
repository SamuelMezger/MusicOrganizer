package jackson;

import junit.framework.TestCase;
import org.junit.Assert;
import somepackage.FlatVideoInfo;

import java.util.Optional;

public class JacksonVideoInfoParserTest extends TestCase {

    public void testFromJson() {
        String flatVideoInfoJson = "{\"_type\": \"url\", \"url\": \"OJdG8wsU8cw\", \"ie_key\": \"Youtube\", \"id\": \"OJdG8wsU8cw\", \"title\": \"TheFatRat & AleXa (\\uc54c\\ub809\\uc0ac) - Rule The World\"}";
        JacksonVideoInfoParser jacksonVideoInfoParser = new JacksonVideoInfoParser();
        Optional<FlatVideoInfo> flatVideoInfoOpt = jacksonVideoInfoParser.fromJson(flatVideoInfoJson);
        flatVideoInfoOpt.ifPresentOrElse(flatVideoInfo -> {
            Assert.assertEquals("OJdG8wsU8cw", flatVideoInfo.getId());
            Assert.assertEquals("TheFatRat & AleXa (알렉사) - Rule The World", flatVideoInfo.getTitle());
        }, () -> Assert.fail("Parsing of FlatVideoInfoJson failed"));
    }
}