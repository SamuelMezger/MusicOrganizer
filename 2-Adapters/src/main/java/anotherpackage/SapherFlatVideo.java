package anotherpackage;

import somepackage.FlatVideoInfo;
import somepackage.MyDownloadProgressCallback;
import somepackage.YoutubeException;

public class SapherFlatVideo implements FlatVideoInfo, Downloadable {
    private final String id;
    private final String title;
    private final YoutubeRequestFactory youtubeRequestFactory;
                
    public SapherFlatVideo(String id, String title, YoutubeRequestFactory youtubeRequestFactory) {
        this.id = id;
        this.title = title;
        this.youtubeRequestFactory = youtubeRequestFactory;
    }
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void download(String destinationFolder, MyDownloadProgressCallback myDownloadProgressCallback) throws YoutubeException {
        YoutubeRequest downloadRequest = this.youtubeRequestFactory.makeRequest(this.id, destinationFolder);
        downloadRequest.setOption("format", "m4a");
        downloadRequest.execute(myDownloadProgressCallback);
    }

    @Override
    public String toString() {
        return "\"" + id + "\", \"" + title + "\"";
    }
}
