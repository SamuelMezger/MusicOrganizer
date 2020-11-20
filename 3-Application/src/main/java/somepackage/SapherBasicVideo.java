package somepackage;


public class SapherBasicVideo implements BasicVideo, Downloadable {
    private final String videoId;
    private final String videoTitle;
    private final YoutubeExtractor youtubeExtractor;
                
    public SapherBasicVideo(String videoId, String videoTitle, YoutubeExtractor youtubeExtractor) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.youtubeExtractor = youtubeExtractor;
    }
    
    @Override
    public void download(String destinationFolder, MyDownloadProgressCallback myDownloadProgressCallback) throws YoutubeException {
        this.youtubeExtractor.downloadAudio(this.videoId, destinationFolder, myDownloadProgressCallback);
    }


    @Override
    public FullVideoInfo getFullVideoInfo() throws YoutubeException {
        return this.youtubeExtractor.getFullVideoInfo(this.videoId);
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
