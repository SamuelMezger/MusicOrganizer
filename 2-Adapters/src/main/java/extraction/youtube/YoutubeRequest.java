package extraction.youtube;

import extraction.MyDownloadProgressCallback;
import extraction.ExtractionException;

public interface YoutubeRequest {
     void setOption(String key);
     void setOption(String key, String value);
     YoutubeResponse execute() throws ExtractionException;
     YoutubeResponse execute(MyDownloadProgressCallback myDownloadProgressCallback) throws ExtractionException;
}
