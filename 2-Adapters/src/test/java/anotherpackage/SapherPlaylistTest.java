package anotherpackage;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import somepackage.BasicVideoInfo;
import somepackage.YoutubeException;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class SapherPlaylistTest {

    private static final String flatPlaylistRawOut = "some Info in Json\nother Info in Json\nnot parsable Json";

    @Test
    public void getBasicVideoInfos() throws YoutubeException {
        YoutubeRequestFactory youtubeRequestFactory = EasyMock.createMock(YoutubeRequestFactory.class);
        BasicVideoInfoParser basicVideoInfoParser = EasyMock.createMock(BasicVideoInfoParser.class);
        SapherPlaylist sapherPlaylist = new SapherPlaylist("someID", youtubeRequestFactory, basicVideoInfoParser);
        YoutubeRequest youtubeRequest = EasyMock.createMock(YoutubeRequest.class);
        EasyMock.expect(youtubeRequestFactory.makeRequest("someID")).andReturn(youtubeRequest);
        
        youtubeRequest.setOption("skip-download");
        youtubeRequest.setOption("flat-playlist");
        youtubeRequest.setOption("dump-json");
        EasyMock.expectLastCall();
        YoutubeResponse youtubeResponse = EasyMock.createMock(YoutubeResponse.class);
        EasyMock.expect(youtubeResponse.getOut()).andReturn(flatPlaylistRawOut);
        EasyMock.expect(youtubeRequest.execute()).andReturn(youtubeResponse);
        SapherBasicVideo someInfo = new SapherBasicVideo("someID", "someTitle", youtubeRequestFactory);
        EasyMock.expect(basicVideoInfoParser.fromJson("some Info in Json")).andReturn(Optional.of(someInfo));
        SapherBasicVideo otherInfo = new SapherBasicVideo("otherID", "otherTitle", youtubeRequestFactory);
        EasyMock.expect(basicVideoInfoParser.fromJson("other Info in Json")).andReturn(Optional.of(otherInfo));
        EasyMock.expect(basicVideoInfoParser.fromJson("not parsable Json")).andReturn(Optional.empty());
        
        EasyMock.replay(youtubeRequestFactory);
        EasyMock.replay(basicVideoInfoParser);
        EasyMock.replay(youtubeRequest);
        EasyMock.replay(youtubeResponse);

        List<BasicVideoInfo> basicVideoInfos = sapherPlaylist.getBasicVideoInfos();
        Assert.assertEquals(2, basicVideoInfos.size());
        Assert.assertTrue(basicVideoInfos.contains(someInfo));
        Assert.assertTrue(basicVideoInfos.contains(otherInfo));
    }
}