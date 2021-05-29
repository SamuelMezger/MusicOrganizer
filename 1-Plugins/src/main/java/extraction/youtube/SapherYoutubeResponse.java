package extraction.youtube;

import com.sapher.youtubedl.YoutubeDLResponse;

public class SapherYoutubeResponse implements YoutubeResponse {
    private final YoutubeDLResponse youtubeDLResponse;

    public SapherYoutubeResponse(YoutubeDLResponse youtubeDLResponse) {
        this.youtubeDLResponse = youtubeDLResponse;
    }

    public String getOut() {
        return this.youtubeDLResponse.getOut();
    }
}
