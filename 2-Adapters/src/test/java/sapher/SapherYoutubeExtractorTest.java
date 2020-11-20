package sapher;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import sapher.*;
import somepackage.*;

import java.util.List;
import java.util.Optional;

public class SapherYoutubeExtractorTest {

    private final static String MusicVid_TheFatRat_RuleTheWorld_ID = "OJdG8wsU8cw"; 
    private final static String MusicVid_TheFatRat_RuleTheWorld_Title = "TheFatRat & AleXa (알렉사) - Rule The World";
    private static final String flatPlaylistRawOut = "some Info in Json\nother Info in Json\nnot parsable Json";


    @Test
    public void testDownloadAudio() throws YoutubeException {
        final float[] downloadProgress = new float[1];

        YoutubeRequestFactory youtubeRequestFactory = EasyMock.createMock(YoutubeRequestFactory.class);
        YoutubeExtractor youtubeExtractor = new SapherYoutubeExtractor(youtubeRequestFactory, EasyMock.createMock(BasicVideoInfoParser.class));

        Capture<MyDownloadProgressCallback> cap = EasyMock.newCapture();

        YoutubeRequest youtubeRequest = EasyMock.createMock(YoutubeRequest.class);
        youtubeRequest.setOption("format", "m4a");
        EasyMock.expectLastCall();

        YoutubeResponse youtubeResponse = EasyMock.createMock(YoutubeResponse.class);
        EasyMock.expect(youtubeRequest.execute(EasyMock.capture(cap))).andReturn(youtubeResponse);
        EasyMock.expect(youtubeRequestFactory.makeRequest(MusicVid_TheFatRat_RuleTheWorld_ID, "/tmp/")).andReturn(youtubeRequest);

        EasyMock.replay(youtubeRequestFactory);
        EasyMock.replay(youtubeRequest);
        EasyMock.replay(youtubeResponse);

        youtubeExtractor.downloadAudio(MusicVid_TheFatRat_RuleTheWorld_ID, "/tmp/", new MyDownloadProgressCallback() {
            @Override
            public void onProgressUpdate(float progress, long etaInSeconds) {
                System.out.println(progress + "%");
                downloadProgress[0] = progress;
            }
        });

        MyDownloadProgressCallback cb = cap.getValue();
        cb.onProgressUpdate(0, 60);
        cb.onProgressUpdate(10, 50);
        cb.onProgressUpdate(50, 30);
        cb.onProgressUpdate(80, 20);
        cb.onProgressUpdate(100, 0);

        Assert.assertEquals(100, downloadProgress[0], 1E-3);
    }

    @Test
    public void getBasicVideoInfos() throws YoutubeException {
        YoutubeRequestFactory youtubeRequestFactory = EasyMock.createMock(YoutubeRequestFactory.class);
        BasicVideoInfoParser basicVideoInfoParser = EasyMock.createMock(BasicVideoInfoParser.class);
        YoutubeExtractor youtubeExtractor = new SapherYoutubeExtractor(youtubeRequestFactory, basicVideoInfoParser);

        YoutubeRequest youtubeRequest = EasyMock.createMock(YoutubeRequest.class);
        EasyMock.expect(youtubeRequestFactory.makeRequest("someID")).andReturn(youtubeRequest);

        youtubeRequest.setOption("skip-download");
        youtubeRequest.setOption("flat-playlist");
        youtubeRequest.setOption("dump-json");
        EasyMock.expectLastCall();
        YoutubeResponse youtubeResponse = EasyMock.createMock(YoutubeResponse.class);
        EasyMock.expect(youtubeResponse.getOut()).andReturn(flatPlaylistRawOut);
        EasyMock.expect(youtubeRequest.execute()).andReturn(youtubeResponse);
        SapherBasicVideo someInfo = new SapherBasicVideo("someID", "someTitle", youtubeExtractor);
        EasyMock.expect(basicVideoInfoParser.fromJson("some Info in Json")).andReturn(Optional.of(someInfo));
        SapherBasicVideo otherInfo = new SapherBasicVideo("otherID", "otherTitle", youtubeExtractor);
        EasyMock.expect(basicVideoInfoParser.fromJson("other Info in Json")).andReturn(Optional.of(otherInfo));
        EasyMock.expect(basicVideoInfoParser.fromJson("not parsable Json")).andReturn(Optional.empty());

        EasyMock.replay(youtubeRequestFactory);
        EasyMock.replay(basicVideoInfoParser);
        EasyMock.replay(youtubeRequest);
        EasyMock.replay(youtubeResponse);

        List<BasicVideoInfo> basicVideoInfos = youtubeExtractor.getBasicVideoInfos("someID");
        Assert.assertEquals(2, basicVideoInfos.size());
        Assert.assertTrue(basicVideoInfos.contains(someInfo));
        Assert.assertTrue(basicVideoInfos.contains(otherInfo));
    }
}