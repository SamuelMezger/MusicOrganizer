package model.youtube;


public class BasicVideoInfo {
    private final String videoId;
    private final String videoTitle;
                
    public BasicVideoInfo(String videoId, String videoTitle) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public String getVideoTitle() {
        return this.videoTitle;
    }

    @Override
    public String toString() {
        return "\"" + this.videoId + "\", \"" + this.videoTitle + "\"";
    }
}
