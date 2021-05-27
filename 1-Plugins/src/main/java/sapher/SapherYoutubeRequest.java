package sapher;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import extraction.MyDownloadProgressCallback;
import extraction.ExtractionException;

public class SapherYoutubeRequest implements YoutubeRequest {
    private final YoutubeDLRequest youtubeDLRequest;

    public SapherYoutubeRequest(String id) {
        this.youtubeDLRequest = new YoutubeDLRequest(id);
    }

    public SapherYoutubeRequest(String id, String destinationFolder) {
        this.youtubeDLRequest = new YoutubeDLRequest(id, destinationFolder);
        
    }

    public void setOption(String key) {
        this.youtubeDLRequest.setOption(key);
    }

    public void setOption(String key, String value) {
        this.youtubeDLRequest.setOption(key, value);
    }

    public YoutubeResponse execute() throws ExtractionException {
        try {
            return new SapherYoutubeResponse(YoutubeDL.execute(this.youtubeDLRequest));
        } catch (YoutubeDLException e) {
            e.printStackTrace();
            throw new ExtractionException(e);
        }
    }

    public YoutubeResponse execute(MyDownloadProgressCallback myDownloadProgressCallback) throws ExtractionException {
        try {
            return new SapherYoutubeResponse(YoutubeDL.execute(this.youtubeDLRequest, myDownloadProgressCallback::onProgressUpdate));
        } catch (YoutubeDLException e) {
            e.printStackTrace();
            throw new ExtractionException(e);
        }
    }
}
