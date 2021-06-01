package extraction.youtube;

import com.sapher.youtubedl.*;
import com.sapher.youtubedl.mapper.VideoInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class YoutubeDLJavaIntegrationTest {

    private final static String MusicVid_TheFatRat_RuleTheWorld_ID = "OJdG8wsU8cw";
    private final static String YTMusic_TheFatRat_RuleTheWorld_ID = "es__dhV4nT8";


    @Test
    public void MusicInVid_extractable() throws YoutubeDLException {
        VideoInfo videoInfo = YoutubeDL.getVideoInfo(MusicVid_TheFatRat_RuleTheWorld_ID);
        Assert.assertEquals(
                "YoutubeDL should be able to extract metadata from the 'Music in this Video Section'",
                "Rule The World, TheFatRat, Rule The World",
                String.format("%s, %s, %s", videoInfo.track, videoInfo.artist, videoInfo.album)
        );
    }
    
    @Test
    public void YTMusic_description_extractable() throws YoutubeDLException {
        VideoInfo videoInfo = YoutubeDL.getVideoInfo(YTMusic_TheFatRat_RuleTheWorld_ID);
        Assert.assertEquals(
                "YoutubeDL should be able to extract metadata from the description of YTMusic tracks",
                "Rule the World, TheFatRat, AleXa, Rule the World",
                String.format("%s, %s, %s", videoInfo.track, videoInfo.artist, videoInfo.album)
        );
    }
}
