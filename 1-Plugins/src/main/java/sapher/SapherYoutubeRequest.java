package sapher;

import anotherpackage.YoutubeRequest;
import anotherpackage.YoutubeResponse;
import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import somepackage.MyDownloadProgressCallback;
import somepackage.YoutubeException;

public class SapherYoutubeRequest implements YoutubeRequest {
    private final YoutubeDLRequest youtubeDLRequest;

    public SapherYoutubeRequest(String id) {
        this.youtubeDLRequest = new YoutubeDLRequest(id);
    }

    public SapherYoutubeRequest(String id, String destinationFolder) {
        this.youtubeDLRequest = new YoutubeDLRequest(id, destinationFolder);
        
    }

    public void setOption(String key) {
        youtubeDLRequest.setOption(key);
    }

    public void setOption(String key, String value) {
        youtubeDLRequest.setOption(key, value);
    }

    public YoutubeResponse execute() throws YoutubeException {
        try {
            return new SapherYoutubeResponse(YoutubeDL.execute(youtubeDLRequest));
        } catch (YoutubeDLException e) {
            e.printStackTrace();
            throw new YoutubeException(e);
        }
    }

    public YoutubeResponse execute(MyDownloadProgressCallback myDownloadProgressCallback) throws YoutubeException {
        try {
            return new SapherYoutubeResponse(YoutubeDL.execute(youtubeDLRequest, myDownloadProgressCallback::onProgressUpdate));
        } catch (YoutubeDLException e) {
            e.printStackTrace();
            throw new YoutubeException(e);
        }
    }
}
