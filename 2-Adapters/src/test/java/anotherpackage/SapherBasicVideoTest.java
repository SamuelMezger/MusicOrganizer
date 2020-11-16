package anotherpackage;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import somepackage.MyDownloadProgressCallback;
import somepackage.YoutubeException;

public class SapherBasicVideoTest {

    private final static String MusicVid_TheFatRat_RuleTheWorld_ID = "OJdG8wsU8cw"; 
    private final static String MusicVid_TheFatRat_RuleTheWorld_Title = "TheFatRat & AleXa (알렉사) - Rule The World"; 

    @Test
    public void testDownload() throws YoutubeException {
        final float[] downloadProgress = new float[1];

        YoutubeRequestFactory youtubeRequestFactory = EasyMock.createMock(YoutubeRequestFactory.class);
        SapherBasicVideo video = new SapherBasicVideo(MusicVid_TheFatRat_RuleTheWorld_ID, MusicVid_TheFatRat_RuleTheWorld_Title, youtubeRequestFactory);

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

        video.download("/tmp/", new MyDownloadProgressCallback() {
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
}