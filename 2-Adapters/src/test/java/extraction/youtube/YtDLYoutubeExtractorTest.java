package extraction.youtube;

import extraction.Downloader;
import model.youtube.BasicVideoInfo;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import extraction.*;

import java.io.FileNotFoundException;
import java.util.List;

public class YtDLYoutubeExtractorTest {

    private final static String MusicVid_TheFatRat_RuleTheWorld_ID = "OJdG8wsU8cw"; 
    private final static String MusicVid_TheFatRat_RuleTheWorld_Title = "TheFatRat & AleXa (알렉사) - Rule The World";
    private static final String flatPlaylistRawOut = "some Info in Json\nother Info in Json";


    @Test
    public void testDownloadAudio() throws ExtractionException {
        final float[] downloadProgress = new float[1];

        YoutubeRequestFactory youtubeRequestFactory = EasyMock.createMock(YoutubeRequestFactory.class);
        Downloader downloader = EasyMock.createMock(Downloader.class);
        YoutubeExtractor youtubeExtractor = new YtDLYoutubeExtractor(youtubeRequestFactory, EasyMock.createMock(BasicVideoInfoParser.class), downloader);

        Capture<ProgressCallback> cap = EasyMock.newCapture();

        YoutubeRequest youtubeRequest = EasyMock.createMock(YoutubeRequest.class);
        youtubeRequest.setOption("format", "m4a");
        EasyMock.expectLastCall();

        YoutubeResponse youtubeResponse = EasyMock.createMock(YoutubeResponse.class);
        EasyMock.expect(youtubeRequest.execute(EasyMock.capture(cap))).andReturn(youtubeResponse);
        EasyMock.expect(youtubeRequestFactory.makeRequest(MusicVid_TheFatRat_RuleTheWorld_ID, "/tmp/")).andReturn(youtubeRequest);

        EasyMock.replay(youtubeRequestFactory);
        EasyMock.replay(youtubeRequest);
        EasyMock.replay(youtubeResponse);

        try {
            youtubeExtractor.downloadAudio(MusicVid_TheFatRat_RuleTheWorld_ID, "/tmp/", (progress, etaInSeconds) -> {
                System.out.println(progress + "%");
                downloadProgress[0] = progress;
            });
        } catch (FileNotFoundException expected) {}

        ProgressCallback cb = cap.getValue();
        cb.onProgressUpdate(0, 60);
        cb.onProgressUpdate(10, 50);
        cb.onProgressUpdate(50, 30);
        cb.onProgressUpdate(80, 20);
        cb.onProgressUpdate(100, 0);

        Assert.assertEquals(100, downloadProgress[0], 1E-3);
    }

    @Test
    public void getBasicVideoInfos() throws ExtractionException {
        YoutubeRequestFactory youtubeRequestFactory = EasyMock.createMock(YoutubeRequestFactory.class);
        BasicVideoInfoParser basicVideoInfoParser = EasyMock.createMock(BasicVideoInfoParser.class);
        Downloader downloader = EasyMock.createMock(Downloader.class);
        YoutubeExtractor youtubeExtractor = new YtDLYoutubeExtractor(youtubeRequestFactory, basicVideoInfoParser, downloader);

        YoutubeRequest youtubeRequest = EasyMock.createMock(YoutubeRequest.class);
        EasyMock.expect(youtubeRequestFactory.makeRequest("someID")).andReturn(youtubeRequest);

        youtubeRequest.setOption("skip-download");
        youtubeRequest.setOption("flat-playlist");
        youtubeRequest.setOption("dump-extraction.json");
        EasyMock.expectLastCall();
        YoutubeResponse youtubeResponse = EasyMock.createMock(YoutubeResponse.class);
        EasyMock.expect(youtubeResponse.getOut()).andReturn(flatPlaylistRawOut);
        EasyMock.expect(youtubeRequest.execute()).andReturn(youtubeResponse);
        BasicVideoInfo someInfo = new BasicVideoInfo("someID", "someTitle");
        EasyMock.expect(basicVideoInfoParser.fromJson("some Info in Json")).andReturn(someInfo);
        BasicVideoInfo otherInfo = new BasicVideoInfo("otherID", "otherTitle");
        EasyMock.expect(basicVideoInfoParser.fromJson("other Info in Json")).andReturn(otherInfo);

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