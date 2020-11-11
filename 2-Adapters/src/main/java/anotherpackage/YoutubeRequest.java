package anotherpackage;

import somepackage.MyDownloadProgressCallback;
import somepackage.YoutubeException;

public interface YoutubeRequest {
     void setOption(String key);
     void setOption(String key, String value);
     YoutubeResponse execute() throws YoutubeException;
     YoutubeResponse execute(MyDownloadProgressCallback myDownloadProgressCallback) throws YoutubeException;
}
