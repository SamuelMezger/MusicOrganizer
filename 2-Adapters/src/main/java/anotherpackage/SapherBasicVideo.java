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
    
//    @Override
//    public void download(String destinationFolder, MyDownloadProgressCallback myDownloadProgressCallback) throws YoutubeException {
//        youtubeRequestFactory.download(videoId, destinationFolder, myDownloadProgressCallback);
//    }
    
    @Override
    public void download(String destinationFolder, MyDownloadProgressCallback myDownloadProgressCallback) throws YoutubeException {
        YoutubeRequest downloadRequest = this.youtubeRequestFactory.makeRequest(this.videoId, destinationFolder);
        downloadRequest.setOption("format", "m4a");
        downloadRequest.execute(myDownloadProgressCallback);
    }


    @Override
    public FullVideoInfo getFullVideoInfo() throws YoutubeException {
        return youtubeRequestFactory.getFullVideoInfo(videoId);
    }

    @Override
    public String getVideoId() {
        return videoId;
    }

    @Override
    public String getVideoTitle() {
        return videoTitle;
    }

    @Override
    public String toString() {
        return "\"" + videoId + "\", \"" + videoTitle + "\"";
    }
}
