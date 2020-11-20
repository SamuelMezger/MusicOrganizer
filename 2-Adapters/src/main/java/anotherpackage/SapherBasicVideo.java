package anotherpackage;

import somepackage.BasicVideo;
import somepackage.FullVideoInfo;
import somepackage.MyDownloadProgressCallback;
import somepackage.YoutubeException;


public class SapherBasicVideo implements BasicVideo, Downloadable {
    private final String videoId;
    private final String videoTitle;
    private final YoutubeRequestFactory youtubeRequestFactory;
                
    public SapherBasicVideo(String videoId, String videoTitle, YoutubeRequestFactory youtubeRequestFactory) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.youtubeRequestFactory = youtubeRequestFactory;
    }
    
    @Override
    public void download(String destinationFolder, MyDownloadProgressCallback myDownloadProgressCallback) throws YoutubeException {
        this.youtubeRequestFactory.downloadAudio(this.videoId, destinationFolder, myDownloadProgressCallback);
    }


    @Override
    public FullVideoInfo getFullVideoInfo() throws YoutubeException {
        return this.youtubeRequestFactory.getFullVideoInfo(this.videoId);
    }

    @Override
    public String getVideoId() {
        return this.videoId;
    }

    @Override
    public String getVideoTitle() {
        return this.videoTitle;
    }

    @Override
    public String toString() {
        return "\"" + this.videoId + "\", \"" + this.videoTitle + "\"";
    }
}
